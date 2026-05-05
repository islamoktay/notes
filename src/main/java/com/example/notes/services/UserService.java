package com.example.notes.services;

import com.example.notes.dtos.PageResponse;
import com.example.notes.dtos.UserRequest;
import com.example.notes.dtos.UserResponse;
import com.example.notes.entities.User;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.ResourceNotFoundException;
import com.example.notes.mappers.UserMapper;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private User mapUserRequestToUser(UserRequest userRequest) {
        String name = userRequest.name();
        if (name == null || name.trim().isEmpty()) {
            return new User("", "");
        }
        
        name = name.trim();
        int spaceIdx = name.indexOf(" ");
        if (spaceIdx > 0) {
            return new User(name.substring(0, spaceIdx), name.substring(spaceIdx + 1));
        }
        return new User(name, "");
    }

    public PageResponse<UserResponse> getUsers(Pageable pageable) {
        log.info("Fetching paginated active users");
        Page<User> userPage = userRepository.findAll(pageable);
        return userMapper.toResponsePage(userPage);
    }

    @Transactional
    public UserResponse addUser(UserRequest userRequest) {
        User savedUser = userRepository.save(mapUserRequestToUser(userRequest));
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Soft-deleting user with ID: {}", id);

        User user = userRepository.findByIdWithNotes(id)
                .orElseThrow(() -> {
                    log.warn("Cannot delete user: ID {} not found or already deleted", id);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
                });

        int notesDeleted = user.getNotes().size();
        user.getNotes().forEach(note -> note.setDeleted(true));
        user.setDeleted(true);

        userRepository.save(user);
        log.info("Successfully soft-deleted user ID: {} along with {} note(s)", id, notesDeleted);
    }
}
