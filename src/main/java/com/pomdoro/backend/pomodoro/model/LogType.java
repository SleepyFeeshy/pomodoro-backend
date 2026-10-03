package com.pomdoro.backend.pomodoro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "session_types")
public class LogType {
    @Id
    private UUID id;

    private int default_duration;
    private boolean is_default;
    private OffsetDateTime created_at;
    private OffsetDateTime updated_at;
    private OffsetDateTime synced_at;
    private OffsetDateTime deleted_at;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getDefault_duration() {
        return default_duration;
    }

    public void setDefault_duration(int default_duration) {
        this.default_duration = default_duration;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at) {
        this.created_at = created_at;
    }

    public OffsetDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(OffsetDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public OffsetDateTime getSynced_at() {
        return synced_at;
    }

    public void setSynced_at(OffsetDateTime synced_at) {
        this.synced_at = synced_at;
    }

    public OffsetDateTime getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(OffsetDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }
}
