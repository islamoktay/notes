package com.example.notes.services;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.exceptions.ApiRequestException;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    // Convert Entity to DTO
    private NoteResponse mapToResponse(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getUser().getId()
        );
    }

    public List<NoteResponse> getNotes() {
        return noteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NoteResponse> getUserNotes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getNotes().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void saveNote(NoteRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ApiRequestException("User not found"));

        Note note = new Note(request.title(), request.content());
        user.addNote(note);
        userRepository.save(user);
    }
}
