package com.example.notes.dtos;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}