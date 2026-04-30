package com.example.notes.mappers;

import com.example.notes.dtos.UserResponse;
import com.example.notes.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getName()
        );
    }
}
