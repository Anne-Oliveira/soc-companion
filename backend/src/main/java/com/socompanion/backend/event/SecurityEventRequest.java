package com.socompanion.backend.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record SecurityEventRequest(
        @NotNull EventType eventType,
        @Size(max = 45) String sourceIp,
        @Size(max = 100) String username,
        @Size(max = 500) String message,
        @NotBlank @Size(max = 100) String source,
        @NotNull Instant occurredAt) { }
