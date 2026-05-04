package com.example.notes.services;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.dtos.PageResponse;
import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.ResourceNotFoundException;
import com.example.notes.mappers.NoteMapper;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteMapper noteMapper;

    public PageResponse<NoteResponse> getNotes(Pageable pageable) {
        log.info("Fetching paginated active notes from database");
        Page<Note> notePage = noteRepository.findAllByDeletedFalse(pageable);
        return noteMapper.toResponsePage(notePage);
    }

    public PageResponse<NoteResponse> getUserNotes(Long userId, Pageable pageable) {
        log.info("Fetching paginated active notes for user ID: {}", userId);
        
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            log.warn("Attempted to fetch notes for non-existing or deleted user ID: {}", userId);
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        Page<Note> notePage = noteRepository.findAllByUserIdAndDeletedFalse(userId, pageable);
        return noteMapper.toResponsePage(notePage);
    }

    @Transactional
    public NoteResponse saveNote(NoteRequest request) {
        log.info("Saving new note for user ID: {}", request.userId());
        User user = userRepository.findByIdAndDeletedFalse(request.userId())
                .orElseThrow(() -> {
                    log.warn("Cannot save note: User ID {} not found or deleted", request.userId());
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
                });

        Note note = new Note(request.title(), request.content());
        note.setUser(user);
        
        Note savedNote = noteRepository.save(note);
        log.info("Successfully saved note with ID: {}", savedNote.getId());
        return noteMapper.toResponse(savedNote);
    }

    @Transactional
    public void deleteNote(Long id) {
        log.info("Deleting note with ID: {}", id);
        Note note = noteRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.warn("Cannot delete note: ID {} not found or already deleted", id);
                    return new ResourceNotFoundException(ErrorCode.NOTE_NOT_FOUND);
                });
        
        note.setDeleted(true);
        noteRepository.save(note);
        log.info("Successfully manually soft-deleted note with ID: {}", id);
    }
}
