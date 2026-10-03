package com.pomdoro.backend.pomodoro.service;

import com.pomdoro.backend.pomodoro.model.LogType;
import com.pomdoro.backend.pomodoro.repository.LogTypeRepository;
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
