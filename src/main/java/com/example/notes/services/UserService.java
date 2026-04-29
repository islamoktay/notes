package com.example.notes.services;

import com.example.notes.dtos.NoteResponse;
import com.example.notes.dtos.UserRequest;
import com.example.notes.dtos.UserResponse;
import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private NoteResponse mapNoteToNoteResponse(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getUser().getId()
        );
    }

    private UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getNotes().stream().map(this::mapNoteToNoteResponse).toList()
        );
    }

    private User mapUserRequestToUser(UserRequest userRequest) {
        return new User(
                userRequest.name()
        );
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(this::mapUserToUserResponse).toList();
    }

    public void addUser(UserRequest userRequest) {
        userRepository.save(mapUserRequestToUser(userRequest));
    }
}
