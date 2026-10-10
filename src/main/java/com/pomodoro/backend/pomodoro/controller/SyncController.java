package com.pomodoro.backend.pomodoro.controller;

import com.pomodoro.backend.pomodoro.model.OutboxRequest;
import com.pomodoro.backend.pomodoro.service.SyncService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
public class SyncController {
    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/push")
    public void syncPush(
            @RequestBody OutboxRequest outboxRequest
    ) {
        syncService.push(outboxRequest);
    }
}
