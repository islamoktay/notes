package com.example.notes.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRequestV2(
        @NotBlank(message = "{validation.name.required}") String firstName,
        @NotBlank(message = "{validation.name.required}") String lastName
) {}
