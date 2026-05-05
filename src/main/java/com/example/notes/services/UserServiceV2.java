package com.example.notes.services;

import com.example.notes.dtos.PageResponse;
import com.example.notes.dtos.UserRequestV2;
import com.example.notes.dtos.UserResponseV2;
import com.example.notes.entities.User;
import com.example.notes.mappers.UserMapperV2;
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
public class UserServiceV2 {
    private final UserRepository userRepository;
    private final UserMapperV2 userMapperV2;

    public PageResponse<UserResponseV2> getUsersV2(Pageable pageable) {
        log.info("Fetching paginated active users for V2");
        Page<User> userPage = userRepository.findAll(pageable);
        return userMapperV2.toResponsePage(userPage);
    }

    @Transactional
    public UserResponseV2 addUserV2(UserRequestV2 userRequestV2) {
        log.info("Adding user V2: {} {}", userRequestV2.firstName(), userRequestV2.lastName());
        User user = userMapperV2.toEntity(userRequestV2);
        User savedUser = userRepository.save(user);
        return userMapperV2.toResponse(savedUser);
    }
}
