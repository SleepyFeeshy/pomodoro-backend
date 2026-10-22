package com.pomodoro.backend.auth.model;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class Role {
    @NotBlank
    UUID id;

    @NotBlank String roleName;
}
