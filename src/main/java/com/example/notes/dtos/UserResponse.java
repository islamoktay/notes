package com.example.notes.dtos;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        List<NoteResponse> notes
) {}