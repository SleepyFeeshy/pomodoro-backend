package com.pomodoro.backend.sync.service;

import tools.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.UUID;

public interface SyncTableHandler {
    String table();
    Optional<Long> upsert(UUID userId, UUID id, JsonNode data);
    Optional<Long> softDelete(UUID userId, UUID id, JsonNode data);
    Optional<JsonNode> findRow(UUID userId, UUID id);
    Optional<Long> currentSeq(UUID userId, UUID id);
}