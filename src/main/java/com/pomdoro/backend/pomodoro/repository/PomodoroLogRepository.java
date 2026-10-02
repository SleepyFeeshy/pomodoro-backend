package com.pomdoro.backend.pomodoro.repository;

import com.pomdoro.backend.pomodoro.model.PomodoroLog;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PomodoroLogRepository extends CrudRepository<PomodoroLog, UUID> {
}
