package com.pomodoro.backend.auth.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name="user_auth_credentials")
public class UserAuthCredential {
    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("provider")
    private String provider;

    @Column("provider_user_id")
    private String providerUserId;

    @Column("identifier")
    private String identifier;

    @Column("password_hash")
    private String passwordHash;

    @Column("last_login_at")
    private OffsetDateTime lastLoginAt;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public UserAuthCredential(UUID userId, String provider, String providerUserId, String identifier, String passwordHash, OffsetDateTime lastLoginAt, OffsetDateTime createdAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.identifier = identifier;
        this.passwordHash = passwordHash;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public OffsetDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(OffsetDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
