package com.example.notes.mappers;

import com.example.notes.dtos.NoteResponse;
import com.example.notes.entities.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteResponse toResponse(Note note) {
        if (note == null) return null;
        
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getUser() != null ? note.getUser().getId() : null
        );
    }
}
