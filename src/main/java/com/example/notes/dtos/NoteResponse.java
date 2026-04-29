package com.example.notes.dtos;

public record NoteResponse(
        Long id,
        String title,
        String content,
        Long userId
) {}