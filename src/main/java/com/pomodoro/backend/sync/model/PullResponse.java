package com.pomodoro.backend.sync.model;

import java.util.List;

public record PullResponse(
        List<PullChange> changes,
        long nextCursor,     // client saves this and sends it as ?since= next time
        boolean hasMore      // true = call again immediately
) {}
