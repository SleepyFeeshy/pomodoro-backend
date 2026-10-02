package com.pomdoro.backend.pomodoro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "sessions")
public class PomodoroLog {
    @Id
    private UUID id;

    private OffsetDateTime finished_at;
    private double duration;
    private UUID session_type_id;

    private OffsetDateTime deleted_at;
    private OffsetDateTime updated_at;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(OffsetDateTime finished_at) {
        this.finished_at = finished_at;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public UUID getSession_type_id() {
        return session_type_id;
    }

    public void setSession_type_id(UUID session_type_id) {
        this.session_type_id = session_type_id;
    }

    public OffsetDateTime getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(OffsetDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public OffsetDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(OffsetDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
