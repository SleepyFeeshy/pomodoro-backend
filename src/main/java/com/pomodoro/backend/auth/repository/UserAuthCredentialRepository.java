package com.pomodoro.backend.auth.repository;

import com.pomodoro.backend.auth.model.UserAuthCredential;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface UserAuthCredentialRepository extends CrudRepository<UserAuthCredential, UUID> {
    @Modifying
    @Query("""
    INSERT INTO user_auth_credentials (user_id, provider, provider_user_id, identifier, last_login_at)
    VALUES (:user_id, :provider, :provider_user_id, :identifier, :last_login_at)
    ON CONFLICT (provider, provider_user_id) DO NOTHING
    """)
    void upsertUserAuthCredential(UUID user_id, String provider, String provider_user_id, String identifier, OffsetDateTime last_login_at);
}
