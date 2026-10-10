package com.pomodoro.backend.pomodoro.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.JsonNode;

import java.util.List;

public record OutboxRequest(
        @NotBlank String deviceId,
        @NotEmpty @Size(max = 200) List<@Valid OutboxEntry> changes
) {}