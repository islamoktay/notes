package com.example.notes;

import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuditingIntegrationTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should automatically set createdAt and updatedAt when saving entity")
    void shouldSetAuditingFields() {
        // GIVEN
        User user = new User("Audit", "User");
        user.setEmail("audit@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Note note = new Note("Audit Title", "Audit Content");
        note.setUser(user);

        // WHEN
        Note savedNote = noteRepository.save(note);

        // THEN
        assertThat(savedNote.getCreatedAt()).isNotNull();
        assertThat(savedNote.getUpdatedAt()).isNotNull();
        assertThat(savedNote.getCreatedAt()).isEqualTo(savedNote.getUpdatedAt());
    }
}
