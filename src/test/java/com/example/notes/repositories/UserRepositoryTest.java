package com.example.notes.repositories;

import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🎓 Educational Note: Integration Testing with @DataJpaTest
 * 
 * Unlike Unit Tests, here we WANT to test the database layer.
 * 
 * - `@DataJpaTest` starts a mini Spring context with only JPA components.
 * - By default, it looks for an in-memory database like H2.
 * - This allows us to test if our SQL queries (like JOIN FETCH) actually work.
 * 
 * 🚀 These tests now validate both:
 * - `@SQLRestriction("deleted = false")` auto-filtering on entities
 * - `findByIdWithNotes` for safe eager loading of a single user's notes
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Test
    @DisplayName("Should fetch a user with their notes using findByIdWithNotes (JOIN FETCH)")
    void findByIdWithNotes_Success() {
        // GIVEN: Save a user and a note to the H2 database
        User user = new User("Jane", "Doe");
        userRepository.save(user);

        Note note = new Note("Secret Note", "This is a secret");
        user.addNote(note);
        userRepository.save(user);

        // Crucial for testing: Flush and clear the persistence context!
        entityManager.flush();
        entityManager.clear();

        // WHEN: Call the new single-entity fetch method
        Optional<User> found = userRepository.findByIdWithNotes(user.getId());

        // THEN: Verify the data was fetched correctly
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Jane");
        assertThat(found.get().getNotes()).hasSize(1);
        assertThat(found.get().getNotes().get(0).getTitle()).isEqualTo("Secret Note");
    }

    @Test
    @DisplayName("Should auto-filter deleted users via @SQLRestriction")
    void findAll_ShouldExcludeDeletedUsers() {
        // GIVEN: Create two users, soft-delete one
        User activeUser = new User("Active", "User");
        userRepository.save(activeUser);

        User deletedUser = new User("Deleted", "User");
        deletedUser.setDeleted(true);
        userRepository.save(deletedUser);

        entityManager.flush();
        entityManager.clear();

        // WHEN: Query all users via standard JpaRepository method
        Page<User> usersPage = userRepository.findAll(PageRequest.of(0, 10));

        // THEN: Only the active user should appear — @SQLRestriction filters automatically
        assertThat(usersPage.getContent()).hasSize(1);
        assertThat(usersPage.getContent().get(0).getFirstName()).isEqualTo("Active");
    }

    @Test
    @DisplayName("Should auto-filter deleted notes from a user's collection via @SQLRestriction")
    void findByIdWithNotes_ShouldExcludeDeletedNotes() {
        // GIVEN: Create a user with two notes, soft-delete one note
        User user = new User("Note", "Owner");
        user.addNote(new Note("Active Note", "Visible content"));
        user.addNote(new Note("Deleted Note", "Hidden content"));
        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        // Soft-delete one note directly via SQL to bypass @SQLRestriction
        entityManager.createNativeQuery("UPDATE notes SET deleted = true WHERE title = 'Deleted Note'")
                .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        // WHEN: Fetch user with notes
        Optional<User> found = userRepository.findByIdWithNotes(user.getId());

        // THEN: Only the active note should be visible
        assertThat(found).isPresent();
        assertThat(found.get().getNotes()).hasSize(1);
        assertThat(found.get().getNotes().get(0).getTitle()).isEqualTo("Active Note");
    }
}
