package com.pomodoro.backend.sync.service;

import com.pomodoro.backend.sync.repository.SessionSyncRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionSyncHandler implements SyncTableHandler{
    private final NamedParameterJdbcTemplate jdbc;
    private final SessionSyncRepository sessionSyncRepository;
    private final ObjectMapper objectMapper;

    SessionSyncHandler(
            NamedParameterJdbcTemplate jdbc,
            SessionSyncRepository sessionSyncRepository,
            ObjectMapper objectMapper) {
        this.sessionSyncRepository = sessionSyncRepository;
        this.jdbc = jdbc;
        this.objectMapper = objectMapper;
    }

    @Override
    public String table() {
        return "sessions";
    }

    @Override
    public Optional<Long> upsert(UUID userId, UUID id, JsonNode data) {
        return sessionSyncRepository.upsertIfNewer(
                id,
                userId,
                UUID.fromString(required(data, "session_type_id").asString()),
                OffsetDateTime.parse(required(data, "finished_at").asString()),
                required(data, "duration").asLong(),
                OffsetDateTime.parse(required(data, "updated_at").asString()),
                optionalTimestamp(data, "deleted_at"));
    }

    @Override
    public Optional<Long> softDelete(UUID userId, UUID id, JsonNode data) {
        return Optional.empty();
    }

    @Override
    public Optional<JsonNode> findRow(UUID userId, UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> currentSeq(UUID userId, UUID id) {
        return Optional.empty();
    }

    // ---- payload helpers ----

    private static JsonNode required(JsonNode data, String field) {
        JsonNode node = data.get(field);
        if (node == null || node.isNull()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return node;
    }

    private static OffsetDateTime optionalTimestamp(JsonNode data, String field) {
        JsonNode node = data.get(field);
        return (node == null || node.isNull()) ? null : OffsetDateTime.parse(node.asString());
    }
}
