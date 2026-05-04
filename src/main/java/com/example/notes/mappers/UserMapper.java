package com.example.notes.mappers;

import com.example.notes.dtos.PageResponse;
import com.example.notes.dtos.UserResponse;
import com.example.notes.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public PageResponse<UserResponse> toResponsePage(Page<User> userPage) {
        List<UserResponse> content = userPage.getContent().stream()
                .map(this::toResponse)
                .toList();
        
        return PageResponse.from(userPage, content);
    }
}
