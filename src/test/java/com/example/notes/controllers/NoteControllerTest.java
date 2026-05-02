package com.example.notes.controllers;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.exceptions.ErrorCode;
import com.example.notes.exceptions.ResourceNotFoundException;
import com.example.notes.services.NoteService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 🎓 Educational Note: Controller Testing with @WebMvcTest
 * 
 * This tests the "Web Layer" (Controllers, Routing, JSON conversion).
 * 
 * - `@WebMvcTest` does NOT start the whole server. It only starts the web part.
 * - We use `MockMvc` to "fake" HTTP requests to our API.
 * - We mock the Service layer because we already tested it in NoteServiceTest.
 * 
 * 🚀 Professional Addition: Structured Responses
 * - We now verify that our data is wrapped in a consistent "ApiResponse" object.
 * - We check for "$.success" and "$.data" instead of looking at the root level.
 */
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return list of notes wrapped in ApiResponse")
    void getNotes_Success() throws Exception {
        // GIVEN
        NoteResponse note = new NoteResponse(1L, "Title", "Content", 1L);
        given(noteService.getNotes()).willReturn(List.of(note));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Title"))
                .andExpect(jsonPath("$.data[0].content").value("Content"));
    }

    @Test
    @DisplayName("Should create note and return wrapped response")
    void saveNote_Success() throws Exception {
        // GIVEN
        NoteRequest request = new NoteRequest("New Note", "Some content", 1L);
        NoteResponse response = new NoteResponse(1L, "New Note", "Some content", 1L);
        
        given(noteService.saveNote(any(NoteRequest.class))).willReturn(response);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Note created successfully"))
                .andExpect(jsonPath("$.data.title").value("New Note"));
    }

    @Test
    @DisplayName("Should return structured error response when validation fails")
    void saveNote_InvalidData() throws Exception {
        // GIVEN: Request with blank title
        NoteRequest request = new NoteRequest("", "Some content", 1L);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    @DisplayName("Should return structured error response when user is not found")
    void getUserNotes_UserNotFound() throws Exception {
        // GIVEN
        given(noteService.getUserNotes(99L))
                .willThrow(new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/notes/user/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getDefaultMessage()));
    }
}
