package com.example.notes.controllers;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.dtos.PageResponse;
import com.example.notes.services.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import com.example.notes.security.JwtAuthenticationFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return paginated list of notes wrapped in ApiResponse")
    void getNotes_Success() throws Exception {
        // GIVEN
        NoteResponse note = new NoteResponse(1L, "Title", "Content", 1L, null, null);
        PageResponse<NoteResponse> pageResponse = new PageResponse<>(List.of(note), 0, 10, 1, 1, true);
        given(noteService.getMyNotes(any(Pageable.class))).willReturn(pageResponse);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("Title"))
                .andExpect(jsonPath("$.data.content[0].content").value("Content"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    @DisplayName("Should create note and return wrapped response")
    void saveNote_Success() throws Exception {
        // GIVEN
        NoteRequest request = new NoteRequest("New Note", "Some content");
        NoteResponse response = new NoteResponse(1L, "New Note", "Some content", 1L, null, null);
        
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
        NoteRequest request = new NoteRequest("", "Some content");

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.errors.title").exists());
    }
}
