package com.example.notes.repositories;

import com.example.notes.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByDeletedFalse(Pageable pageable);
    Page<Note> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);
    Optional<Note> findByIdAndDeletedFalse(Long id);
}
