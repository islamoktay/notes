package com.example.notes.mappers;

import com.example.notes.dtos.NoteResponse;
import com.example.notes.dtos.PageResponse;
import com.example.notes.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteMapper {

    public NoteResponse toResponse(Note note) {
        if (note == null) return null;
        
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getUser() != null ? note.getUser().getId() : null,
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }

    public PageResponse<NoteResponse> toResponsePage(Page<Note> notePage) {
        List<NoteResponse> content = notePage.getContent().stream()
                .map(this::toResponse)
                .toList();
        
        return PageResponse.from(notePage, content);
    }
}
