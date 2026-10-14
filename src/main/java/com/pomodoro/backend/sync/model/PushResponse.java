package com.pomodoro.backend.sync.model;

import java.util.List;

public record PushResponse(List<PushResult> results) {}