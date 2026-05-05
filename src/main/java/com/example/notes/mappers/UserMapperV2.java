package com.example.notes.mappers;

import com.example.notes.dtos.PageResponse;
import com.example.notes.dtos.UserResponseV2;
import com.example.notes.dtos.UserRequestV2;
import com.example.notes.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapperV2 {

    public User toEntity(UserRequestV2 request) {
        if (request == null) return null;
        return new User(request.firstName(), request.lastName());
    }

    public UserResponseV2 toResponse(User user) {
        if (user == null) return null;

        return new UserResponseV2(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public PageResponse<UserResponseV2> toResponsePage(Page<User> userPage) {
        List<UserResponseV2> content = userPage.getContent().stream()
                .map(this::toResponse)
                .toList();
        
        return PageResponse.from(userPage, content);
    }
}
