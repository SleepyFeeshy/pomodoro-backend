package com.pomodoro.backend.pomodoro.repository;

import com.pomodoro.backend.pomodoro.model.PomodoroLog;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PomodoroLogRepository extends CrudRepository<PomodoroLog, UUID> {

    @Query("""
        SELECT *
        FROM sessions
        ORDER BY finished_at DESC
    """)
    public Iterable<PomodoroLog> findAllByFinishedAt();
}
