package com.pomodoro.backend.sync.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name="processed_mutations")
public class ProcessedMutation {
        @NotBlank UUID mutationId;
        @NotBlank UUID userId;
        @NotBlank OffsetDateTime processedAt;

        public UUID getMutationId() {
                return mutationId;
        }

        public void setMutationId(UUID mutationId) {
                this.mutationId = mutationId;
        }

        public UUID getUserId() {
                return userId;
        }

        public void setUserId(UUID userId) {
                this.userId = userId;
        }

        public OffsetDateTime getProcessedAt() {
                return processedAt;
        }

        public void setProcessedAt(OffsetDateTime processedAt) {
                this.processedAt = processedAt;
        }
}
