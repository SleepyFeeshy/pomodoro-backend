package com.pomodoro.backend.sync.service;

import com.pomodoro.backend.sync.model.OutboxEntry;
import com.pomodoro.backend.sync.model.OutboxOp;
import com.pomodoro.backend.sync.model.OutboxRequest;
import com.pomodoro.backend.sync.model.PushResult;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SyncService {
    public static Logger logger = Logger.getLogger(SyncService.class.getName());

    private final NamedParameterJdbcTemplate jdbc;
    private final TransactionTemplate tx;
    private final Map<String, SyncTableHandler> handlers;

    // Notes: Spring automatically finds classes that implements SessionSyncHandler beans
    public SyncService(
            NamedParameterJdbcTemplate jdbc,
            TransactionTemplate tx,
            List<SyncTableHandler> handlerList) {
        this.jdbc = jdbc;
        this.tx = tx;
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(SyncTableHandler::table, h -> h));
    }

    /**
     * Applies a batch of outbox entries in order.
     * Deliberately NOT @Transactional: each entry gets its own transaction, because
     * Postgres aborts a whole transaction after any failed statement, and one bad
     * entry must not reject the rest of the batch.
     */

    public void push(OutboxRequest outboxRequest) {
        logger.info(String.valueOf(outboxRequest));
        for (OutboxEntry outboxEntry: outboxRequest.changes()){
            // logger.info(String.valueOf(outboxEntry));
            // Insert mutation ID into processed_mutations
            // Find handler by table

            tx.execute(status -> applyEntry(outboxEntry));
            // Parse payload
            //
        };
    }

    /** Runs inside the per-entry transaction. Any exception thrown here rolls it back. */
    private PushResult applyEntry(OutboxEntry entry) {
        SyncTableHandler handler = handlers.get(entry.table());
        UUID rowId = UUID.fromString(entry.id());
        UUID userId = UUID.fromString("af24efdc-7387-4b9b-bd8c-bd183b393205");

        // 1. Idempotency: a replayed mutation is acknowledged but not applied again.
//        if (!recordMutation(userId, entry.mutationId())) {
//            return PushResult.duplicate(
//                    entry.mutationId(),
//                    handler.currentSeq(userId, rowId).orElse(null));
//        }

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
}
