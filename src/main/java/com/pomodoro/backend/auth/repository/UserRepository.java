package com.pomodoro.backend.auth.repository;

import com.pomodoro.backend.auth.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    @Query("""
    INSERT INTO users (email)
    VALUES (:email)
    ON CONFLICT (email)
            DO UPDATE SET email = excluded.email
    RETURNING id
    """)
    UUID upsertUser(String email);
}
