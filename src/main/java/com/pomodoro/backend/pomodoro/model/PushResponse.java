package com.pomodoro.backend.pomodoro.model;

import java.util.List;

public record PushResponse(List<PushResult> results) {}