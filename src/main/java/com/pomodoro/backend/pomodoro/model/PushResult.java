package com.pomodoro.backend.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PushResult(
        String mutationId,
        PushStatus status,
        Long serverSeq,        // set for applied, duplicate, conflict
        String message,        // set for rejected
        JsonNode serverRow     // set for conflict: current server row, snake_case keys
) {
    public static PushResult applied(String mutationId, long serverSeq) {
        return new PushResult(mutationId, PushStatus.APPLIED, serverSeq, null, null);
    }

    public static PushResult duplicate(String mutationId, Long serverSeq) {
        return new PushResult(mutationId, PushStatus.DUPLICATE, serverSeq, null, null);
    }

    public static PushResult conflict(String mutationId, JsonNode serverRow) {
        return new PushResult(mutationId, PushStatus.CONFLICT,
                serverRow.path("server_seq").asLong(), null, serverRow);
    }

    public static PushResult rejected(String mutationId, String message) {
        return new PushResult(mutationId, PushStatus.REJECTED, null, message, null);
    }
}