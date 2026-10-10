package com.pomodoro.backend.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PushStatus {
    @JsonProperty("applied")   APPLIED,    // written; serverSeq is the row's new version
    @JsonProperty("duplicate") DUPLICATE,  // mutation already processed; safe to drop from the outbox
    @JsonProperty("conflict")  CONFLICT,   // lost last-write-wins; serverRow holds the winner
    @JsonProperty("rejected")  REJECTED    // invalid; message says why
}