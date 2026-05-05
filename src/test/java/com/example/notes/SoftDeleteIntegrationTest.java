package com.example.notes;

import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import com.example.notes.services.NoteService;
import com.example.notes.services.UserService;
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
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should hide note from queries but keep it in database when soft-deleted")
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

        // 3. THEN: @SQLRestriction auto-filters — findById should NOT find it
        Optional<Note> foundNote = noteRepository.findById(noteId);
        assertThat(foundNote).isEmpty();

        // 4. THEN: Direct SQL check should show it still exists with deleted = true
        Boolean deleted = jdbcTemplate.queryForObject(
                "SELECT deleted FROM notes WHERE id = ?",
                Boolean.class,
                noteId
        );
        assertThat(deleted).isTrue();
    }

    @Test
    @DisplayName("Should cascade soft-delete from User to all their Notes")
    @Transactional
    void shouldCascadeSoftDeleteFromUserToNotes() {
        // 1. GIVEN: Create a user with 3 notes
        User user = new User("Parent User");
        user.addNote(new Note("Note 1", "Content 1"));
        user.addNote(new Note("Note 2", "Content 2"));
        user.addNote(new Note("Note 3", "Content 3"));
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        entityManager.flush();
        entityManager.clear();

        // 2. WHEN: Soft-delete the user via the service
        userService.deleteUser(userId);
        
        entityManager.flush();
        entityManager.clear();

        // 3. THEN: User should be invisible to standard queries (@SQLRestriction)
        Optional<User> foundUser = userRepository.findById(userId);
        assertThat(foundUser).isEmpty();

        // 4. THEN: All notes should also be invisible to standard queries
        long visibleNotes = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM notes WHERE user_id = ? AND deleted = false",
                Long.class,
                userId
        );
        assertThat(visibleNotes).isZero();

        // 5. THEN: But all notes still exist in the database (soft-deleted, not hard-deleted)
        long totalNotes = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM notes WHERE user_id = ?",
                Long.class,
                userId
        );
        assertThat(totalNotes).isEqualTo(3);
    }
}
