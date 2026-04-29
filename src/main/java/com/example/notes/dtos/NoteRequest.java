package com.example.notes.dtos;

public record NoteRequest(
        String title,
        String content,
        Long userId
) {}