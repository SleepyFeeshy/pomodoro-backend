package com.pomodoro.backend.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OutboxOp {
    @JsonProperty("upsert") UPSERT,
    @JsonProperty("delete") DELETE
}