package com.example.notes.repositories;

import com.example.notes.entities.Note;
import com.example.notes.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🎓 Educational Note: Integration Testing with @DataJpaTest
 * 
 * Unlike Unit Tests, here we WANT to test the database layer.
 * 
 * - `@DataJpaTest` starts a mini Spring context with only JPA components.
 * - By default, it looks for an in-memory database like H2.
 * - This allows us to test if our SQL queries (like JOIN FETCH) actually work.
 */
@DataJpaTest
@ActiveProfiles("test") // We can use this to point to a specific application-test.yaml if needed
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Test
    @DisplayName("Should find users with their notes using FETCH JOIN")
    void findAllWithNotes_Success() {
        // GIVEN: Save a user and a note to the H2 database
        User user = new User("Jane Doe");
        userRepository.save(user);

        Note note = new Note("Secret Note", "This is a secret");
        user.addNote(note); // Use the helper method to keep both sides in sync!
        userRepository.save(user); // Saving user will now save the note too because of CascadeType.ALL

        // Crucial for testing custom queries: Flush and clear the context!
        // This forces Hibernate to actually execute the SQL query instead of returning cached objects.
        entityManager.flush();
        entityManager.clear();

        // WHEN: Call the custom query method
        List<User> users = userRepository.findAllWithNotes();

        // THEN: Verify the data was fetched correctly
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("Jane Doe");
        assertThat(users.get(0).getNotes()).hasSize(1);
        assertThat(users.get(0).getNotes().get(0).getTitle()).isEqualTo("Secret Note");
    }
}
