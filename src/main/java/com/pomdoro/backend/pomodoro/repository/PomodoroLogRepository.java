package com.pomdoro.backend.pomodoro.repository;

import com.pomdoro.backend.pomodoro.model.PomodoroLog;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PomodoroLogRepository extends CrudRepository<PomodoroLog, UUID> {

    @Query("""
        SELECT *
        FROM sessions
        ORDER BY finished_at DESC
    """)
    public Iterable<PomodoroLog> findAllByFinishedAt();
}
