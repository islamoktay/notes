package com.example.notes.validations;

import com.example.notes.dtos.NoteRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentTitleAndContentValidator implements ConstraintValidator<DifferentTitleAndContent, NoteRequest> {

    @Override
    public boolean isValid(NoteRequest noteRequest, ConstraintValidatorContext context) {
        if (noteRequest == null || noteRequest.title() == null || noteRequest.content() == null) {
            return true; // Let @NotBlank handle null or empty values
        }
        return !noteRequest.title().trim().equalsIgnoreCase(noteRequest.content().trim());
    }
}
