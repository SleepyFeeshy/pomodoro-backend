package com.pomodoro.backend.sync.service;

import com.pomodoro.backend.sync.model.*;
import com.pomodoro.backend.sync.repository.ProcessedMutationRepository;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SyncService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SyncService.class);
    public static Logger logger = Logger.getLogger(SyncService.class.getName());
    private final NamedParameterJdbcTemplate jdbc;
    private final TransactionTemplate tx;
    private final Map<String, SyncTableHandler> handlers;
    private final ObjectMapper objectMapper;

    private final ProcessedMutationRepository processedMutationRepository;

    // Notes: Spring automatically finds classes that implements SessionSyncHandler beans
    public SyncService(
            NamedParameterJdbcTemplate jdbc,
            TransactionTemplate tx,
            ProcessedMutationRepository processedMutationRepository,
            ObjectMapper objectMapper,
            List<SyncTableHandler> handlerList) {
        this.jdbc = jdbc;
        this.tx = tx;
        this.objectMapper = objectMapper;
        this.processedMutationRepository = processedMutationRepository;
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(SyncTableHandler::table, h -> h));
    }

    /**
     * Applies a batch of outbox entries in order.
     * Deliberately NOT @Transactional: each entry gets its own transaction, because
     * Postgres aborts a whole transaction after any failed statement, and one bad
     * entry must not reject the rest of the batch.
     */

    public PushResponse push(OutboxRequest outboxRequest) {
        List<PushResult> results = new ArrayList<>(outboxRequest.changes().size());
        logger.info(String.valueOf(outboxRequest));
        for (OutboxEntry outboxEntry: outboxRequest.changes()){
            try {
                results.add(tx.execute(status -> applyEntry(outboxEntry)));
            } catch (Exception e) {
                // Rolled back, including the processed_mutations row, so the client can
                // fix the cause and the same entry can be retried.
                String reason = e.getMessage();
                logger.warning(
                        "Rejected mutation " + outboxEntry.mutationId() +
                        "(" +  outboxEntry.op().toString() + " " + outboxEntry.table() + " " + outboxEntry.id() + ")" + reason);
                results.add(PushResult.rejected(outboxEntry.mutationId(), reason));
            }
        };
        return new PushResponse(results);
    }

    /** Runs inside the per-entry transaction. Any exception thrown here rolls it back. */
    private PushResult applyEntry(OutboxEntry entry) {
        SyncTableHandler handler = handlers.get(entry.table());
        UUID rowId = UUID.fromString(entry.id());
        UUID userId = UUID.fromString("af24efdc-7387-4b9b-bd8c-bd183b393205");

        // 1. Idempotency: a replayed mutation is acknowledged but not applied again.
        if (recordMutation(UUID.fromString(entry.mutationId()), userId) == 0) {
            logger.info("Duplicate");
            return PushResult.duplicate(
                    entry.mutationId(),
                    handler.currentSeq(userId, rowId).orElse(null));
        }

        // 2. Apply. The handler's SQL enforces both last-write-wins (updated_at is newer)
        //    and ownership (user_id matches), and returns the row's new server_seq.
        Optional<Long> serverSeq = entry.op() == OutboxOp.DELETE
                ? handler.softDelete(userId, rowId, entry.data())
                : handler.upsert(userId, rowId, entry.data());

        if (serverSeq.isPresent()) {
            return PushResult.applied(entry.mutationId(), serverSeq.get());
        }

        // 3. Nothing was written: the incoming change lost. Hand back the server's row
        //    so the client can adopt it.
        JsonNode serverRow = handler.findRow(userId, rowId)
                .orElseThrow(() -> new IllegalStateException(
                        "row not found, or not owned by this user"));
        return PushResult.conflict(entry.mutationId(), serverRow);
    }

    public PullResponse pull(UUID userId, long since, int limit) {
        int MAX_PAGE_SIZE = 50;
        int pageSize = Math.min(Math.max(limit, 1), MAX_PAGE_SIZE);

        // Ask for one extra row: if it comes back, we know another page exists
        List<PullChange> rows = jdbc.query(
                PULL_SQL,
                Map.of("userId", userId, "since", since, "limit", pageSize + 1),
                (rs, rowNum) -> new PullChange(
                        rs.getString("tbl"),
                        rs.getString("id"),
                        rs.getLong("server_seq"),
                        objectMapper.readTree(rs.getString("data"))));

        boolean hasMore = rows.size() > pageSize;
        List<PullChange> page = hasMore ? rows.subList(0, pageSize) : rows;
        long nextCursor = page.isEmpty() ? since : page.get(page.size() - 1).serverSeq();

        return new PullResponse(page, nextCursor, hasMore);
    }

    // One UNION ALL branch per synced table. All tables share change_seq, so ordering the
    // merged result by server_seq gives one consistent cursor across tables.
    private static final String PULL_SQL = """
        SELECT tbl, id, server_seq, data FROM (
            SELECT 'sessions' AS tbl, s.id::text AS id, s.server_seq, to_jsonb(s)::text AS data
              FROM sessions s
             WHERE s.user_id = :userId AND s.server_seq > :since
            UNION ALL
            SELECT 'session_types', t.id::text, t.server_seq, to_jsonb(t)::text
              FROM session_types t
             WHERE t.server_seq > :since
        ) changes
        ORDER BY server_seq
        LIMIT :limit
        """;

    // Process the mutation
    private int recordMutation(UUID mutationId, UUID userId) {
        // Query returns number of rows
        // 1 or 0
        return processedMutationRepository.insertProcessedMutation(mutationId, userId);
    }
}
