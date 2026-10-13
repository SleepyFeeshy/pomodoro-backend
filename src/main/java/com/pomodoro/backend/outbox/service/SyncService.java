package com.pomodoro.backend.outbox.service;

import com.pomodoro.backend.outbox.model.OutboxRequest;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SyncService {
    public static Logger logger = Logger.getLogger(SyncService.class.getName());

    /**
     * Applies a batch of outbox entries in order.
     * Deliberately NOT @Transactional: each entry gets its own transaction, because
     * Postgres aborts a whole transaction after any failed statement, and one bad
     * entry must not reject the rest of the batch.
     */

    public void push(OutboxRequest outboxRequest) {

    }
}
