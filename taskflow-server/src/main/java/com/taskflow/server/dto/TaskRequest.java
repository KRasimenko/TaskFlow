package com.taskflow.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TaskRequest(
        @NotBlank String title,
        String description,
        String category,
        String priority,
        String status,
        @NotNull LocalDate dueDate,
        Boolean starred
) {}
