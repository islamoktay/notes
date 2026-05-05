package com.example.notes.dtos;

import java.time.LocalDateTime;

public record UserResponseV2(
        Long id,
        String firstName,
        String lastName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
