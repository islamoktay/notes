package com.example.notes.controllers;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public List<NoteResponse> getNotes() {
        return noteService.getNotes();
    }

    @GetMapping("/user/{userId}")
    public List<NoteResponse> getUserNotes(@PathVariable Long userId) {
        return noteService.getUserNotes(userId);
    }

    @PostMapping
    public ResponseEntity<Void> saveNote(@RequestBody NoteRequest noteRequest) {
        noteService.saveNote(noteRequest);
        return ResponseEntity.status(201).build();
    }
}