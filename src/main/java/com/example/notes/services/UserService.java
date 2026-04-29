package com.example.notes.services;

import com.example.notes.dtos.UserRequest;
import com.example.notes.dtos.UserResponse;
import com.example.notes.entities.User;
import com.example.notes.mappers.UserMapper;
import com.example.notes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private User mapUserRequestToUser(UserRequest userRequest) {
        return new User(
                userRequest.name()
        );
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAllWithNotes().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse addUser(UserRequest userRequest) {
        User savedUser = userRepository.save(mapUserRequestToUser(userRequest));
        return userMapper.toResponse(savedUser);
    }
}
