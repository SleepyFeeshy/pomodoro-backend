package com.pomodoro.backend.sync.service;

import com.pomodoro.backend.sync.model.*;
import com.pomodoro.backend.sync.repository.ProcessedMutationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncServiceUnitTests {

    private SyncService syncService;

    //    Dependencies
    private SessionSyncHandler sessionSyncHandler;

    @Mock private NamedParameterJdbcTemplate jdbc;
    @Mock private SyncTableHandler sessionsHandler;
    @Mock private PlatformTransactionManager txManager;
    @Mock private ProcessedMutationRepository processedMutationRepository;

    private final JsonMapper mapper = JsonMapper.builder().build();

    @BeforeEach
    void setUp() {
        when(sessionsHandler.table()).thenReturn("sessions");
        TransactionTemplate tx = new TransactionTemplate(txManager);

        syncService = new SyncService(jdbc, tx, processedMutationRepository, List.of(sessionsHandler));
    }

    @Test
    @DisplayName("""
            GIVEN One valid OutboxRequest containing a duplicated OutboxEntry upsert
            WHEN syncService.push is called
            THEN the outbox entry is applied
            """)
    public void pushHappyFlow() {
        // ARRANGE
        // Create sample outbox entry
        UUID mutation1 = UUID.randomUUID();
        UUID row1 = UUID.randomUUID();

        // Outbox Entry json payload
        JsonNode upsertData = mapper.readTree("""
                {
                  "session_type_id": "%s",
                  "finished_at": "2026-10-14T09:00:00Z",
                  "duration": 1500,
                  "updated_at": "2026-10-14T09:00:00Z",
                  "deleted_at": null
                }
                """.formatted(UUID.randomUUID()));
        OutboxEntry upsertEntry = new OutboxEntry(
        mutation1.toString(), "sessions", row1.toString(),
        OutboxOp.UPSERT, upsertData, null);

        OutboxRequest request = new OutboxRequest("device-1", List.of(upsertEntry));
        // Upsert then return value of server_seq as 41
        when(processedMutationRepository.insertProcessedMutation(eq(mutation1), any(UUID.class)))
                .thenReturn(1);
        when(sessionsHandler.upsert(any(UUID.class), eq(row1), eq(upsertData)))
                .thenReturn(Optional.of(41L));
        // ACT
        PushResponse response = syncService.push(request);

        // ASSERT
        assertThat(response.results()).hasSize(1);
        PushResult result = response.results().get(0);
        assertThat(result.mutationId()).isEqualTo(mutation1.toString());
        assertThat(result.status()).isEqualTo(PushStatus.APPLIED);
        assertThat(result.serverSeq()).isEqualTo(41L);

        // ASSERT: the side effects
        verify(txManager).commit(any());                       // the entry's transaction committed
        verify(txManager, never()).rollback(any());
        verify(processedMutationRepository)
                .insertProcessedMutation(eq(mutation1), any(UUID.class));
        verify(sessionsHandler, never()).softDelete(any(), any(), any());
        verify(sessionsHandler, never()).findRow(any(), any());   // only used on conflicts
    }

    @Test
    @DisplayName("""
            GIVEN One valid OutboxRequest containing a valid OutboxEntry upsert
            WHEN syncService.push is called
            THEN the outbox entry is applied
            """)
    public void pushDuplicate() {
        // ARRANGE
        // Create sample outbox entry
        UUID mutation1 = UUID.randomUUID();
        UUID row1 = UUID.randomUUID();

        // Outbox Entry json payload
        JsonNode upsertData = mapper.readTree("""
                {
                  "session_type_id": "%s",
                  "finished_at": "2026-10-14T09:00:00Z",
                  "duration": 1500,
                  "updated_at": "2026-10-14T09:00:00Z",
                  "deleted_at": null
                }
                """.formatted(UUID.randomUUID()));
        OutboxEntry upsertEntry = new OutboxEntry(
                mutation1.toString(), "sessions", row1.toString(),
                OutboxOp.UPSERT, upsertData, null);

        OutboxRequest request = new OutboxRequest("device-1", List.of(upsertEntry));
        // Already processed
        when(processedMutationRepository.insertProcessedMutation(eq(mutation1), any(UUID.class)))
                .thenReturn(0);
        when(sessionsHandler.currentSeq(any(UUID.class), eq(row1)))
                .thenReturn(Optional.of(40L));
        // ACT
        PushResponse response = syncService.push(request);

        // ASSERT
        assertThat(response.results()).hasSize(1);
        PushResult result = response.results().get(0);
        assertThat(result.serverSeq()).isEqualTo(40L);
        assertThat(result.mutationId()).isEqualTo(mutation1.toString());
        assertThat(result.status()).isEqualTo(PushStatus.DUPLICATE);
    }
}
