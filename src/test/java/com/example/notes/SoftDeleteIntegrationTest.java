package com.example.notes;

import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import com.example.notes.services.NoteService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SoftDeleteIntegrationTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should hide note from explicit queries but keep it in database when manually deleted")
    @Transactional
    void shouldSoftDeleteNoteManually() {
        // 1. GIVEN: Create a user and a note
        User user = userRepository.save(new User("Manual Delete User"));
        Note note = new Note("Title", "Content");
        note.setUser(user);
        Note savedNote = noteRepository.save(note);
        Long noteId = savedNote.getId();

        // 2. WHEN: Delete the note via the SERVICE (which handles manual soft delete)
        noteService.deleteNote(noteId);
        
        entityManager.flush();
        entityManager.clear();

        // 3. THEN: findByIdAndDeletedFalse should not find it
        Optional<Note> foundNote = noteRepository.findByIdAndDeletedFalse(noteId);
        assertThat(foundNote).isEmpty();

        // 4. THEN: Direct SQL check should show it still exists with deleted = true
        Boolean deleted = jdbcTemplate.queryForObject(
                "SELECT deleted FROM notes WHERE id = ?",
                Boolean.class,
                noteId
        );
        assertThat(deleted).isTrue();
    }
}
