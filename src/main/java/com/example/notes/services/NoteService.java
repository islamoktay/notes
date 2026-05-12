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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public PageResponse<NoteResponse> getMyNotes(Pageable pageable) {
        User user = getCurrentUser();
        log.info("Fetching paginated active notes for user ID: {}", user.getId());
        Page<Note> notePage = noteRepository.findAllByUserId(user.getId(), pageable);
        return noteMapper.toResponsePage(notePage);
    }

    @Transactional
    public NoteResponse saveNote(NoteRequest request) {
        User user = getCurrentUser();
        log.info("Saving new note for user ID: {}", user.getId());

        Note note = new Note(request.title(), request.content());
        note.setUser(user);
        
        Note savedNote = noteRepository.save(note);
        log.info("Successfully saved note with ID: {}", savedNote.getId());
        return noteMapper.toResponse(savedNote);
    }

    @Transactional
    public void deleteNote(Long id) {
        User user = getCurrentUser();
        log.info("User {} attempting to delete note with ID: {}", user.getId(), id);
        Note note = noteRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> {
                    log.warn("Cannot delete note: ID {} not found, already deleted, or belongs to another user", id);
                    return new ResourceNotFoundException(ErrorCode.NOTE_NOT_FOUND);
                });
        
        note.setDeleted(true);
        noteRepository.save(note);
        log.info("Successfully soft-deleted note with ID: {}", id);
    }
}
