package com.example.notes.controllers;

import com.example.notes.dtos.ApiResponse;
import com.example.notes.dtos.PageResponse;
import com.example.notes.dtos.UserResponseV2;
import com.example.notes.services.UserServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.example.notes.dtos.UserRequestV2;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users V2", description = "Endpoints for managing users (Version 2)")
@Slf4j
public class UserControllerV2 {
    private final UserServiceV2 userServiceV2;

    // Strategy 1: URI Versioning
    @GetMapping(path = "api/v2/users")
    @Operation(summary = "Get all users (URI Versioning)", description = "Returns a paginated list of V2 users using URI versioning")
    public ResponseEntity<ApiResponse<PageResponse<UserResponseV2>>> getUsersV2URI(@ParameterObject Pageable pageable) {
        log.info("REST request to get all users V2 (URI versioning)");
        return ResponseEntity.ok(ApiResponse.success(userServiceV2.getUsersV2(pageable)));
    }

    // Strategy 2: Header Versioning
    @GetMapping(path = "api/v1/users", headers = "X-API-Version=2")
    @Operation(summary = "Get all users (Header Versioning)", description = "Returns a paginated list of V2 users using Header versioning")
    public ResponseEntity<ApiResponse<PageResponse<UserResponseV2>>> getUsersV2Header(@ParameterObject Pageable pageable) {
        log.info("REST request to get all users V2 (Header versioning)");
        return ResponseEntity.ok(ApiResponse.success(userServiceV2.getUsersV2(pageable)));
    }

    // Strategy 3: Media Type / Content Negotiation Versioning
    @GetMapping(path = "api/v1/users", produces = "application/vnd.notes.v2+json")
    @Operation(summary = "Get all users (Media Type Versioning)", description = "Returns a paginated list of V2 users using Media Type versioning")
    public ResponseEntity<ApiResponse<PageResponse<UserResponseV2>>> getUsersV2MediaType(@ParameterObject Pageable pageable) {
        log.info("REST request to get all users V2 (Media Type versioning)");
        return ResponseEntity.ok(ApiResponse.success(userServiceV2.getUsersV2(pageable)));
    }

    // --- POST Endpoints ---

    // Strategy 1: URI Versioning
    @PostMapping(path = "api/v2/users")
    @Operation(summary = "Create a new user (URI Versioning)", description = "Saves a new user to the database and returns V2 response")
    public ResponseEntity<ApiResponse<UserResponseV2>> addUserV2URI(@Valid @RequestBody UserRequestV2 userRequest) {
        log.info("REST request to add user V2 (URI versioning): {} {}", userRequest.firstName(), userRequest.lastName());
        UserResponseV2 savedUser = userServiceV2.addUserV2(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedUser, "User created successfully"));
    }

    // Strategy 2: Header Versioning
    @PostMapping(path = "api/v1/users", headers = "X-API-Version=2")
    @Operation(summary = "Create a new user (Header Versioning)", description = "Saves a new user to the database and returns V2 response")
    public ResponseEntity<ApiResponse<UserResponseV2>> addUserV2Header(@Valid @RequestBody UserRequestV2 userRequest) {
        log.info("REST request to add user V2 (Header versioning): {} {}", userRequest.firstName(), userRequest.lastName());
        UserResponseV2 savedUser = userServiceV2.addUserV2(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedUser, "User created successfully"));
    }

    // Strategy 3: Media Type Versioning
    @PostMapping(path = "api/v1/users", consumes = "application/vnd.notes.v2+json", produces = "application/vnd.notes.v2+json")
    @Operation(summary = "Create a new user (Media Type Versioning)", description = "Saves a new user to the database and returns V2 response")
    public ResponseEntity<ApiResponse<UserResponseV2>> addUserV2MediaType(@Valid @RequestBody UserRequestV2 userRequest) {
        log.info("REST request to add user V2 (Media Type versioning): {} {}", userRequest.firstName(), userRequest.lastName());
        UserResponseV2 savedUser = userServiceV2.addUserV2(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedUser, "User created successfully"));
    }
}
