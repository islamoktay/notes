package com.example.notes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record NoteResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Grocery List") String title,
        @Schema(example = "Buy milk, eggs, and bread") String content,
        @Schema(example = "1") Long userId
) {}