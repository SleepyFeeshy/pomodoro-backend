package com.pomodoro.backend.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OutboxOp {
    @JsonProperty("upsert") UPSERT,
    @JsonProperty("delete") DELETE
}