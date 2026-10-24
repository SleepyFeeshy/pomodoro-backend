package com.pomodoro.backend.auth.controller;

import com.pomodoro.backend.auth.model.User;
import com.pomodoro.backend.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }
}
