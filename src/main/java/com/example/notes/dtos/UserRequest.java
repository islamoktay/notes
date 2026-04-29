package com.example.notes.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
                @NotBlank(message = "Name is required") String name) {
}