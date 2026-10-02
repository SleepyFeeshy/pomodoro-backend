package com.pomdoro.backend.pomodoro.controller;

import com.pomdoro.backend.pomodoro.model.PomodoroLog;
import com.pomdoro.backend.pomodoro.repository.PomodoroLogRepository;
import com.pomdoro.backend.pomodoro.service.PomodoroLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class PomodoroLogController {

    private final PomodoroLogService pomodoroLogService;

    public PomodoroLogController(PomodoroLogService pomodoroLogService) {
        this.pomodoroLogService = pomodoroLogService;
    }

    @GetMapping
    public Iterable<PomodoroLog> getPomodoroLogs() {
        return pomodoroLogService.getAllLogs();
    }
}
