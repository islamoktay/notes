package com.example.notes.repositories;

import com.example.notes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.notes n WHERE u.deleted = false AND (n IS NULL OR n.deleted = false)")
    List<User> findAllWithNotes();
    List<User> findAllByDeletedFalse();
    Optional<User> findByIdAndDeletedFalse(Long id);
}
