package com.pomodoro.backend.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name="users")
public class User {
    @NotBlank
    UUID id;

    @NotBlank @Email
    String email;

    @NotBlank @DateTimeFormat
    OffsetDateTime created_at;

    public User(String email, OffsetDateTime created_at) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.created_at = created_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at) {
        this.created_at = created_at;
    }
}
