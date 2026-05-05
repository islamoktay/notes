package com.example.notes.dtos;

import com.example.notes.validations.DifferentTitleAndContent;
import com.example.notes.validations.NoProfanity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@DifferentTitleAndContent
public record NoteRequest(
        @NotBlank(message = "{validation.title.required}")
        @NoProfanity
        @Schema(example = "Grocery List", description = "The title of the note")
        String title,

        @NotBlank(message = "{validation.content.required}")
        @NoProfanity
        @Schema(example = "Buy milk, eggs, and bread", description = "the body of the note")
        String content,

        @NotNull(message = "{validation.user_id.required}")
        @Schema(example = "1", description = "The ID of the user who owns this note")
        Long userId
) {}