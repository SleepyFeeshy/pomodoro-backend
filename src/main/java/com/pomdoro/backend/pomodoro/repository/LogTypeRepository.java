package com.pomdoro.backend.pomodoro.repository;

import com.pomdoro.backend.pomodoro.model.LogType;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LogTypeRepository extends CrudRepository<LogType, UUID> {
}
