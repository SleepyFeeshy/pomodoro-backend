package com.pomodoro.backend.sync.controller;

import com.pomodoro.backend.sync.model.OutboxRequest;
import com.pomodoro.backend.sync.service.SyncService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody OutboxRequest outboxRequest
    ) {
        syncService.push(outboxRequest);
    }
}
