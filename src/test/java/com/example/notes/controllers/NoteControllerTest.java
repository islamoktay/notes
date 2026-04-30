package com.example.notes.controllers;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
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
 */
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // In Spring Boot 3.4+, @MockBean is replaced by @MockitoBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return list of notes")
    void getNotes_Success() throws Exception {
        // GIVEN
        NoteResponse note = new NoteResponse(1L, "Title", "Content", 1L);
        given(noteService.getNotes()).willReturn(List.of(note));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$[0].content").value("Content"));
    }

    @Test
    @DisplayName("Should create note when data is valid")
    void saveNote_Success() throws Exception {
        // GIVEN
        NoteRequest request = new NoteRequest("New Note", "Some content", 1L);
        NoteResponse response = new NoteResponse(1L, "New Note", "Some content", 1L);
        
        given(noteService.saveNote(any(NoteRequest.class))).willReturn(response);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // Or isOk() depending on your controller
                .andExpect(jsonPath("$.title").value("New Note"));
    }

    @Test
    @DisplayName("Should return 400 when title is blank")
    void saveNote_InvalidData() throws Exception {
        // GIVEN: Request with blank title
        NoteRequest request = new NoteRequest("", "Some content", 1L);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Validation should fail
    }
}
