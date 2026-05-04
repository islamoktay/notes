package com.example.notes.repositories;

import com.example.notes.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByDeletedFalse();
    Optional<Note> findByIdAndDeletedFalse(Long id);
}
