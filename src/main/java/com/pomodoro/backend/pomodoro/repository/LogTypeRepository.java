package com.pomodoro.backend.pomodoro.repository;

import com.pomodoro.backend.pomodoro.model.LogType;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LogTypeRepository extends CrudRepository<LogType, UUID> {
}
