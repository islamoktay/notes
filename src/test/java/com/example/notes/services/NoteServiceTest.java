package com.example.notes.services;

import com.example.notes.dtos.NoteRequest;
import com.example.notes.dtos.NoteResponse;
import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.exceptions.ApiRequestException;
import com.example.notes.mappers.NoteMapper;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 🎓 Educational Note: Unit Testing with Mockito
 * 
 * In Unit Tests, we want to test a single class (NoteService) in isolation.
 * We don't want to start the database or create real users.
 * 
 * Instead, we "Mock" (fake) the dependencies (Repositories, Mappers).
 */
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteService noteService;

    @Test
    @DisplayName("Should save note successfully when user exists")
    void saveNote_Success() {
        // --- 1. GIVEN (Setup the scenario) ---
        // We prepare a fake request
        NoteRequest request = new NoteRequest("Title", "Content", 1L);
        
        // We prepare a fake user that our "mock" repository will return
        User user = new User("John Doe");
        user.setId(1L);

        // We tell Mockito: "When userRepository.findById(1L) is called, return this user"
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // We prepare what the saved note and response will look like
        Note savedNote = new Note("Title", "Content");
        savedNote.setId(1L);
        savedNote.setUser(user);
        NoteResponse response = new NoteResponse(1L, "Title", "Content", 1L);

        given(noteRepository.save(any(Note.class))).willReturn(savedNote);
        given(noteMapper.toResponse(savedNote)).willReturn(response);

        // --- 2. WHEN (Execute the action) ---
        NoteResponse result = noteService.saveNote(request);

        // --- 3. THEN (Verify the outcome) ---
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Title");
        
        // Also verify that the repository's save method was actually called
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when saving note for non-existing user")
    void saveNote_UserNotFound() {
        // GIVEN
        NoteRequest request = new NoteRequest("Title", "Content", 99L);
        
        // Tell Mockito to return "Empty" (User not found)
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        // WHEN & THEN
        // We expect an ApiRequestException to be thrown
        assertThatThrownBy(() -> noteService.saveNote(request))
                .isInstanceOf(ApiRequestException.class)
                .hasMessage("User not found");
    }
}
