package com.example.notes.mappers;

import com.example.notes.dtos.UserResponse;
import com.example.notes.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final NoteMapper noteMapper;

    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getNotes() != null 
                    ? user.getNotes().stream().map(noteMapper::toResponse).toList() 
                    : java.util.Collections.emptyList()
        );
    }
}
