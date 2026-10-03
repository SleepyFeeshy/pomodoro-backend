package com.pomodoro.backend.pomodoro.service;

import com.pomodoro.backend.pomodoro.model.LogType;
import com.pomodoro.backend.pomodoro.repository.LogTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class LogTypeService {
    private final LogTypeRepository logTypeRepository;

    public LogTypeService(LogTypeRepository logTypeRepository) {
        this.logTypeRepository = logTypeRepository;
    }

    public Iterable<LogType> getAllLogTypes() {
        return logTypeRepository.findAll();
    }
}
