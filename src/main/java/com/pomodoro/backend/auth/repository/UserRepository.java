package com.pomodoro.backend.auth.repository;

import com.pomodoro.backend.auth.model.Role;
import com.pomodoro.backend.auth.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("""
        SELECT *
        from users
        where email = :email
    """)
    User findUserByEmail(String email);

    @Query("""
        SELECT r.role_name
        FROM roles r
        JOIN user_roles ur ON r.id = ur.role_id
        JOIN users u ON ur.user_id = u.id
        WHERE lower(u.email) = lower(:email)
    """)
    List<String> findRolesOfUserByEmail(String email);

    @Query("""
        SELECT r.role_name
        FROM roles r
        JOIN user_roles ur ON r.id = ur.role_id
        JOIN users u ON ur.user_id = u.id
        WHERE lower(u.id) = lower(:uuid)
    """)
    List<String> findRolesOfUserById(UUID uuid);
}
