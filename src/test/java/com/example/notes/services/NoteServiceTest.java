package com.example.notes.services;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.dtos.PageResponse;
import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.mappers.NoteMapper;
import com.example.notes.repositories.NoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteService noteService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("John", "Doe");
        mockUser.setId(1L);
        mockUser.setEmail("john.doe@example.com");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should fetch paginated my notes successfully")
    void getMyNotes_Success() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        Note note = new Note("Title", "Content");
        Page<Note> notePage = new PageImpl<>(List.of(note));
        NoteResponse response = new NoteResponse(1L, "Title", "Content", 1L, null, null);
        PageResponse<NoteResponse> pageResponse = new PageResponse<>(List.of(response), 0, 10, 1, 1, true);
        
        given(noteRepository.findAllByUserId(mockUser.getId(), pageable)).willReturn(notePage);
        given(noteMapper.toResponsePage(notePage)).willReturn(pageResponse);

        // WHEN
        PageResponse<NoteResponse> result = noteService.getMyNotes(pageable);

        // THEN
        assertThat(result.content()).hasSize(1);
        assertThat(result.totalElements()).isEqualTo(1);
        verify(noteRepository).findAllByUserId(mockUser.getId(), pageable);
        verify(noteMapper).toResponsePage(notePage);
    }

    @Test
    @DisplayName("Should save note successfully")
    void saveNote_Success() {
        NoteRequest request = new NoteRequest("Title", "Content");

        Note savedNote = new Note("Title", "Content");
        savedNote.setId(1L);
        savedNote.setUser(mockUser);
        NoteResponse response = new NoteResponse(1L, "Title", "Content", 1L, null, null);

        given(noteRepository.save(any(Note.class))).willReturn(savedNote);
        given(noteMapper.toResponse(savedNote)).willReturn(response);

        NoteResponse result = noteService.saveNote(request);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Title");
        verify(noteRepository).save(any(Note.class));
    }
}
