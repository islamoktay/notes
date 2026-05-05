package com.example.notes.repositories;

import com.example.notes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Modifying
    @Query(value = "DELETE FROM users WHERE deleted = true AND updated_at < :thresholdDate", nativeQuery = true)
    int deleteSoftDeletedUsersOlderThan(@Param("thresholdDate") LocalDateTime thresholdDate);
}
