package com.pomodoro.backend.sync.repository;

import com.pomodoro.backend.pomodoro.model.PomodoroLog;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

// Session = whichever entity maps the `sessions` table; Spring Data needs a domain type here
public interface SessionSyncRepository extends Repository<PomodoroLog, UUID> {

    /** Insert, or update if the incoming row is newer and owned by the same user.
     *  Empty result = nothing was written (lost last-write-wins, or another user's row). */
    @Query("""
            INSERT INTO sessions
                (id, user_id, session_type_id, finished_at, duration, updated_at, deleted_at)
            VALUES
                (:id, :userId, :sessionTypeId, :finishedAt, :duration, :updatedAt, :deletedAt)
            ON CONFLICT (id) DO UPDATE SET
                session_type_id = EXCLUDED.session_type_id,
                finished_at     = EXCLUDED.finished_at,
                duration        = EXCLUDED.duration,
                updated_at      = EXCLUDED.updated_at,
                deleted_at      = EXCLUDED.deleted_at
            WHERE sessions.user_id = EXCLUDED.user_id
              AND EXCLUDED.updated_at > sessions.updated_at
            RETURNING server_seq
            """)
    Optional<Long> upsertIfNewer(UUID id,
                                 UUID userId,
                                 UUID sessionTypeId,
                                 OffsetDateTime finishedAt,
                                 long duration,
                                 OffsetDateTime updatedAt,
                                 OffsetDateTime deletedAt);

    @Query("""
            UPDATE sessions
               SET deleted_at = :deletedAt, updated_at = :updatedAt
             WHERE id = :id AND user_id = :userId AND updated_at < :updatedAt
            RETURNING server_seq
            """)
    Optional<Long> softDeleteIfNewer(UUID id, UUID userId,
                                     OffsetDateTime deletedAt, OffsetDateTime updatedAt);

    @Query("SELECT to_jsonb(s)::text FROM sessions s WHERE id = :id AND user_id = :userId")
    Optional<String> findRowJson(UUID id, UUID userId);

    @Query("SELECT server_seq FROM sessions WHERE id = :id AND user_id = :userId")
    Optional<Long> findServerSeq(UUID id, UUID userId);
}