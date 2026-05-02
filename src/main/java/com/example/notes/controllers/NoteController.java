package com.example.notes.controllers;

import com.example.notes.dtos.ApiResponse;
import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.services.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Endpoints for creating and retrieving notes")
@Slf4j
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Get all notes", description = "Returns a list of all notes in the system")
    public ApiResponse<List<NoteResponse>> getNotes() {
        log.info("REST request to get all notes");
        return ApiResponse.success(noteService.getNotes());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notes by User ID", description = "Returns all notes belonging to a specific user")
    public ApiResponse<List<NoteResponse>> getUserNotes(@PathVariable Long userId) {
        log.info("REST request to get notes for user: {}", userId);
        return ApiResponse.success(noteService.getUserNotes(userId));
    }

    @PostMapping
    @Operation(summary = "Create a new note", description = "Saves a new note and connects it to a user")
    public ResponseEntity<ApiResponse<NoteResponse>> saveNote(@Valid @RequestBody NoteRequest noteRequest) {
        log.info("REST request to save note: {}", noteRequest.title());
        NoteResponse savedNote = noteService.saveNote(noteRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedNote, "Note created successfully"));
    }
}