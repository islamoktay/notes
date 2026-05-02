package com.example.notes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record NoteResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Grocery List") String title,
        @Schema(example = "Buy milk, eggs, and bread") String content,
        @Schema(example = "1") Long userId,
        @Schema(example = "2026-05-02T20:00:00") LocalDateTime createdAt,
        @Schema(example = "2026-05-02T20:30:00") LocalDateTime updatedAt
) {}