package com.pomodoro.backend.outbox.model;

import java.util.List;

public record PushResponse(List<PushResult> results) {}