package com.example.notes.repositories;

import com.example.notes.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM notes WHERE deleted = true AND updated_at < :thresholdDate", nativeQuery = true)
    int deleteSoftDeletedNotesOlderThan(@Param("thresholdDate") LocalDateTime thresholdDate);
}
