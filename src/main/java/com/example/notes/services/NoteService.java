package com.example.notes.services;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.exceptions.ApiRequestException;
import com.example.notes.mappers.NoteMapper;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteMapper noteMapper;

    public List<NoteResponse> getNotes() {
        return noteRepository.findAll()
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    public List<NoteResponse> getUserNotes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApiRequestException("User not found"));

        return user.getNotes().stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    @Transactional
    public NoteResponse saveNote(NoteRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ApiRequestException("User not found"));

        Note note = new Note(request.title(), request.content());
        note.setUser(user);
        
        Note savedNote = noteRepository.save(note);
        return noteMapper.toResponse(savedNote);
    }
}
