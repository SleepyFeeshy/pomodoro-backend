package com.pomodoro.backend.pomodoro.controller;

import com.pomodoro.backend.pomodoro.model.PomodoroLog;
import com.pomodoro.backend.pomodoro.service.PomodoroLogService;
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
