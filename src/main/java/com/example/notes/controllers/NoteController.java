package com.example.notes.controllers;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Endpoints for creating and retrieving notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Get all notes", description = "Returns a list of all notes in the system")
    public List<NoteResponse> getNotes() {
        return noteService.getNotes();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notes by User ID", description = "Returns all notes belonging to a specific user")
    public List<NoteResponse> getUserNotes(@PathVariable Long userId) {
        return noteService.getUserNotes(userId);
    }

    @PostMapping
    @Operation(summary = "Create a new note", description = "Saves a new note and connects it to a user")
    public ResponseEntity<NoteResponse> saveNote(@Valid @RequestBody NoteRequest noteRequest) {
        NoteResponse savedNote = noteService.saveNote(noteRequest);
        return ResponseEntity.status(201).body(savedNote);
    }
}