package com.taskflow.server.dto;

import java.time.LocalDate;

public record TaskDto(
        Long id,
        String title,
        String description,
        String category,
        String priority,
        String status,
        LocalDate dueDate,
        boolean starred,
        LocalDate createdAt
) {}
