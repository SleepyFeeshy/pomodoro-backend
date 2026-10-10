package com.pomodoro.backend.pomodoro.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record OutboxEntry(
        @NotBlank String mutationId,   // UUID string, the idempotency key
        @NotBlank String table,        // "sessions", "session_types", ...
        @NotBlank String id,           // UUID string of the affected row
        @NotNull OutboxOp op,
        @NotNull JsonNode data,        // row snapshot, snake_case keys
        Long baseSeq                   // null for rows never synced
) {}