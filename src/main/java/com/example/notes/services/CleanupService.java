package com.example.notes.services;

import com.example.notes.repositories.NoteRepository;
import com.example.notes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Value("${app.cleanup.trash-retention-days:30}")
    private int retentionDays;

    @Scheduled(cron = "${app.cleanup.cron:0 0 3 * * ?}")
    @Transactional
    public void emptyTrash() {
        log.info("Starting scheduled cleanup task: Emptying trash for records older than {} days", retentionDays);

        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(retentionDays);

        // Delete notes first to avoid foreign key constraint violations
        int deletedNotesCount = noteRepository.deleteSoftDeletedNotesOlderThan(thresholdDate);
        log.info("Cleanup task: Permanently deleted {} note(s) from the trash", deletedNotesCount);

        // Then delete users
        int deletedUsersCount = userRepository.deleteSoftDeletedUsersOlderThan(thresholdDate);
        log.info("Cleanup task: Permanently deleted {} user(s) from the trash", deletedUsersCount);

        log.info("Scheduled cleanup task completed successfully");
    }
}
