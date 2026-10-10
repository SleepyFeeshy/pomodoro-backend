package com.pomodoro.backend.pomodoro.model;

import tools.jackson.databind.JsonNode;

public record PullChange(
        String table,        // "sessions", "session_types", ...
        String id,           // row UUID
        long serverSeq,      // this row's version; the client applies it only if newer than what it has
        JsonNode data        // full row as JSON (snake_case keys), tombstones included via deleted_at
) {}