package com.example.notes.services;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.ResourceNotFoundException;
import com.example.notes.mappers.NoteMapper;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteMapper noteMapper;

    public List<NoteResponse> getNotes() {
        log.info("Fetching all notes from database");
        return noteRepository.findAll()
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    public List<NoteResponse> getUserNotes(Long userId) {
        log.info("Fetching notes for user ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.warn("Attempted to fetch notes for non-existing user ID: {}", userId);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
                });

        return user.getNotes().stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    @Transactional
    public NoteResponse saveNote(NoteRequest request) {
        log.info("Saving new note for user ID: {}", request.userId());
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.warn("Cannot save note: User ID {} not found", request.userId());
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
                });

        Note note = new Note(request.title(), request.content());
        note.setUser(user);
        
        Note savedNote = noteRepository.save(note);
        log.info("Successfully saved note with ID: {}", savedNote.getId());
        return noteMapper.toResponse(savedNote);
    }
}
