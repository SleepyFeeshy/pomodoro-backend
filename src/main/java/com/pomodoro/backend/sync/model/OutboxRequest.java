package com.pomodoro.backend.sync.model;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OutboxRequest(
        @NotBlank String deviceId,
        @NotEmpty @Size(max = 200) List<@Valid OutboxEntry> changes
) {}