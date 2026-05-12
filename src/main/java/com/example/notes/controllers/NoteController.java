package com.example.notes.controllers;

import com.example.notes.dtos.ApiResponse;
import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.dtos.PageResponse;
import com.example.notes.services.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Endpoints for creating and retrieving notes")
@Slf4j
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    @Operation(
        summary = "Get my notes", 
        description = "Returns a paginated list of notes for the authenticated user. Sortable fields: id, title, createdAt, updatedAt"
    )
    public ResponseEntity<ApiResponse<PageResponse<NoteResponse>>> getNotes(@ParameterObject Pageable pageable) {
        log.info("REST request to get my notes (paginated)");
        return ResponseEntity.ok(ApiResponse.success(noteService.getMyNotes(pageable)));
    }

    @PostMapping
    @Operation(summary = "Create a new note", description = "Saves a new note for the authenticated user")
    public ResponseEntity<ApiResponse<NoteResponse>> saveNote(@Valid @RequestBody NoteRequest noteRequest) {
        log.info("REST request to save note: {}", noteRequest.title());
        NoteResponse savedNote = noteService.saveNote(noteRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedNote, "Note created successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a note", description = "Performs a soft delete on a note owned by the authenticated user")
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable Long id) {
        log.info("REST request to delete note: {}", id);
        noteService.deleteNote(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Note deleted successfully"));
    }
}