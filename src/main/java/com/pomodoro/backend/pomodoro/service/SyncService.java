package com.pomodoro.backend.pomodoro.service;

import com.pomodoro.backend.pomodoro.model.OutboxRequest;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SyncService {
    public static Logger logger = Logger.getLogger(SyncService.class.getName());

    public void push(OutboxRequest outboxRequest) {
        logger.info("Sync push called");
    }
}
