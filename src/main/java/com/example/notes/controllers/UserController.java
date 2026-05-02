package com.example.notes.controllers;

import com.example.notes.dtos.ApiResponse;
import com.example.notes.dtos.UserRequest;
import com.example.notes.dtos.UserResponse;
import com.example.notes.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.success(userService.getUsers());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> addUser(@Valid @RequestBody UserRequest user) {
        return ApiResponse.success(userService.addUser(user), "User created successfully");
    }
}
