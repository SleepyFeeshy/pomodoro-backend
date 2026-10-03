package com.pomodoro.backend.pomodoro.controller;

import com.pomodoro.backend.pomodoro.model.LogType;
import com.pomodoro.backend.pomodoro.service.LogTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs/types")
public class LogTypeController {

    private final LogTypeService logTypeService;

    public LogTypeController(LogTypeService logTypeService) {
        this.logTypeService = logTypeService;
    }

    @GetMapping
    public Iterable<LogType> getAllLogTypes() {
        return logTypeService.getAllLogTypes();
    }
}
