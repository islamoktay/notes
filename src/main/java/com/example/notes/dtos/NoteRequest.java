package com.example.notes.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public record NoteRequest(
        @NotBlank(message = "Title is required")
        @Schema(example = "Grocery List", description = "The title of the note")
        String title,

        @NotBlank(message = "Content is required")
        @Schema(example = "Buy milk, eggs, and bread", description = "the body of the note")
        String content,

        @NotNull(message = "User ID is required")
        @Schema(example = "1", description = "The ID of the user who owns this note")
        Long userId
) {}