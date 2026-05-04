package com.example.notes.services;

import com.example.notes.dtos.UserRequest;
import com.example.notes.dtos.UserResponse;
import com.example.notes.entities.User;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.ResourceNotFoundException;
import com.example.notes.mappers.UserMapper;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private User mapUserRequestToUser(UserRequest userRequest) {
        return new User(
                userRequest.name()
        );
    }

    public List<UserResponse> getUsers() {
        log.info("Fetching all active users with their active notes");
        return userRepository.findAllWithNotes().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse addUser(UserRequest userRequest) {
        User savedUser = userRepository.save(mapUserRequestToUser(userRequest));
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.warn("Cannot delete user: ID {} not found or already deleted", id);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
                });
        
        user.setDeleted(true);
        userRepository.save(user);
        log.info("Successfully manually soft-deleted user with ID: {}", id);
    }
}
