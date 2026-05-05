package com.example.notes.repositories;

import com.example.notes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Fetches a single user with all their active notes in one query.
     * JOIN FETCH is safe here because we're loading a single entity (no pagination).
     * The @SQLRestriction on the notes collection automatically filters out deleted notes.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.notes WHERE u.id = :id")
    Optional<User> findByIdWithNotes(@Param("id") Long id);
}
