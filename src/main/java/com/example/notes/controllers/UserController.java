package com.example.notes.controllers;

import com.example.notes.dtos.ApiResponse;
import com.example.notes.dtos.PageResponse;
import com.example.notes.dtos.UserRequest;
import com.example.notes.dtos.UserResponse;
import com.example.notes.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(
        summary = "Get all users", 
        description = "Returns a paginated list of all users. Sortable fields: id, name, createdAt"
    )
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(@ParameterObject Pageable pageable) {
        log.info("REST request to get all users (paginated)");
        return ResponseEntity.ok(ApiResponse.success(userService.getUsers(pageable)));
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Saves a new user to the database")
    public ResponseEntity<ApiResponse<UserResponse>> addUser(@Valid @RequestBody UserRequest user) {
        log.info("REST request to add user: {}", user.name());
        UserResponse savedUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedUser, "User created successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Performs a soft delete on a user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("REST request to delete user: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
}
