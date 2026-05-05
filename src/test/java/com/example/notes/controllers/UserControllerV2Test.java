package com.example.notes.controllers;

import com.example.notes.dtos.PageResponse;
import com.example.notes.dtos.UserRequestV2;
import com.example.notes.dtos.UserResponseV2;
import com.example.notes.services.UserServiceV2;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserControllerV2.class)
class UserControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceV2 userServiceV2;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseV2 userResponseV2;
    private PageResponse<UserResponseV2> pageResponse;

    @BeforeEach
    void setUp() {
        userResponseV2 = new UserResponseV2(
                1L,
                "John",
                "Doe",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        pageResponse = new PageResponse<>(
                List.of(userResponseV2),
                1,
                1,
                1L,
                1,
                true
        );
    }

    @Test
    void testGetUsersV2URI() throws Exception {
        Mockito.when(userServiceV2.getUsersV2(any(Pageable.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v2/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].firstName").value("John"));
    }

    @Test
    void testGetUsersV2Header() throws Exception {
        Mockito.when(userServiceV2.getUsersV2(any(Pageable.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/users")
                        .header("X-API-Version", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].firstName").value("John"));
    }

    @Test
    void testGetUsersV2MediaType() throws Exception {
        Mockito.when(userServiceV2.getUsersV2(any(Pageable.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/users")
                        .accept("application/vnd.notes.v2+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].firstName").value("John"));
    }

    @Test
    void testAddUserV2URI() throws Exception {
        UserRequestV2 request = new UserRequestV2("John", "Doe");
        Mockito.when(userServiceV2.addUserV2(any(UserRequestV2.class))).thenReturn(userResponseV2);

        mockMvc.perform(post("/api/v2/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }

    @Test
    void testAddUserV2Header() throws Exception {
        UserRequestV2 request = new UserRequestV2("John", "Doe");
        Mockito.when(userServiceV2.addUserV2(any(UserRequestV2.class))).thenReturn(userResponseV2);

        mockMvc.perform(post("/api/v1/users")
                        .header("X-API-Version", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }

    @Test
    void testAddUserV2MediaType() throws Exception {
        UserRequestV2 request = new UserRequestV2("John", "Doe");
        Mockito.when(userServiceV2.addUserV2(any(UserRequestV2.class))).thenReturn(userResponseV2);

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/vnd.notes.v2+json")
                        .accept("application/vnd.notes.v2+json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }
}
