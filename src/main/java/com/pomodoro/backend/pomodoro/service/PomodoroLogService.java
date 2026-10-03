package com.pomodoro.backend.pomodoro.service;

import com.pomodoro.backend.pomodoro.model.PomodoroLog;
import com.pomodoro.backend.pomodoro.repository.PomodoroLogRepository;
import org.springframework.stereotype.Service;

@Service
public class PomodoroLogService {

    private final PomodoroLogRepository pomodoroLogRepository;

    public PomodoroLogService(PomodoroLogRepository pomodoroLogRepository) {
        this.pomodoroLogRepository = pomodoroLogRepository;
    }

    public Iterable<PomodoroLog> getAllLogs() {
        return pomodoroLogRepository.findAllByFinishedAt();
    }
}
