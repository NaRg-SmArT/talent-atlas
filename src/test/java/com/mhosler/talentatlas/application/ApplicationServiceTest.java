package com.mhosler.TalentAtlas.application;

import com.mhosler.TalentAtlas.application.attention.ApplicationAttentionRule;
import com.mhosler.TalentAtlas.exception.ResourceNotFoundException;
import com.mhosler.TalentAtlas.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationRecordServiceTest {

    @Mock
    private ApplicationRecordRepository applicationRecordRepository;

    @Mock
    private ApplicationRecordMapper applicationRecordMapper;

    @Mock
    private ApplicationAttentionRule attentionRule;

    @InjectMocks
    private ApplicationRecordService applicationRecordService;

    private List<ApplicationAttentionRule> buildAttentionRules() {
        return List.of(attentionRule);
    }

    private ApplicationRecordService buildServiceWithRules() {
        return new ApplicationRecordService(
                applicationRecordRepository,
                applicationRecordMapper,
                buildAttentionRules()
        );
    }

    @Test
    void findAllByUserShouldReturnApplicationsForUser() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        List<ApplicationRecord> expected = List.of(
                new ApplicationRecord(user, "Acme", "Software Engineer", null, null, null, null,
                        ApplicationStatus.APPLIED, LocalDateTime.now(), null, null, null, null)
        );

        when(applicationRecordRepository.findByUser(user)).thenReturn(expected);

        List<ApplicationRecord> result = buildServiceWithRules().findAllByUser(user);

        assertEquals(expected, result);
        verify(applicationRecordRepository).findByUser(user);
    }

    @Test
    void findByIdAndUserShouldReturnApplicationWhenFound() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        ApplicationRecord record = new ApplicationRecord(user, "Acme", "Software Engineer", null, null, null, null,
                ApplicationStatus.APPLIED, LocalDateTime.now(), null, null, null, null);

        when(applicationRecordRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(record));

        ApplicationRecord result = buildServiceWithRules().findByIdAndUser(1L, user);

        assertNotNull(result);
        assertEquals("Acme", result.getCompanyName());
        verify(applicationRecordRepository).findByIdAndUser(1L, user);
    }

    @Test
    void findByIdAndUserShouldThrowWhenApplicationNotFound() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());

        when(applicationRecordRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> buildServiceWithRules().findByIdAndUser(1L, user)
        );

        assertEquals("Application not found: 1", exception.getMessage());
        verify(applicationRecordRepository).findByIdAndUser(1L, user);
    }

    @Test
    void addApplicationRecordShouldSetTimestampsAndSave() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        ApplicationRecord record = new ApplicationRecord(user, "Acme", "Software Engineer", null, null, null, null,
                ApplicationStatus.APPLIED, LocalDateTime.now(), null, null, null, null);

        when(applicationRecordRepository.save(any(ApplicationRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationRecord result = buildServiceWithRules().addApplicationRecord(record);

        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertNotNull(result.getStatusUpdatedAt());
        verify(applicationRecordRepository).save(record);
    }

    @Test
    void updateApplicationRecordShouldUpdateFieldsAndSaveWhenStatusChanges() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());

        LocalDateTime oldStatusUpdatedAt = LocalDateTime.now().minusDays(5);

        ApplicationRecord existing = new ApplicationRecord(
                user, "Old Company", "Old Role", null, null, null, null,
                ApplicationStatus.APPLIED, LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5), oldStatusUpdatedAt, "Old notes"
        );

        ApplicationRecord updated = new ApplicationRecord(
                user, "New Company", "New Role", "http://job.com", "Jane Doe", "1234567890", "jane@example.com",
                ApplicationStatus.INTERVIEW, LocalDateTime.now().minusDays(2),
                null, null, null, "Updated notes"
        );

        when(applicationRecordRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(existing));
        when(applicationRecordRepository.save(existing)).thenReturn(existing);

        ApplicationRecord result = buildServiceWithRules().updateApplicationRecord(1L, updated, user);

        assertEquals("New Company", result.getCompanyName());
        assertEquals("New Role", result.getPositionTitle());
        assertEquals("http://job.com", result.getJobPostingUrl());
        assertEquals("Jane Doe", result.getPrimaryContactName());
        assertEquals("1234567890", result.getPrimaryContactPhone());
        assertEquals("jane@example.com", result.getPrimaryContactEmail());
        assertEquals(ApplicationStatus.INTERVIEW, result.getStatus());
        assertEquals("Updated notes", result.getNotes());
        assertNotNull(result.getUpdatedAt());
        assertTrue(result.getStatusUpdatedAt().isAfter(oldStatusUpdatedAt));
        verify(applicationRecordRepository).findByIdAndUser(1L, user);
        verify(applicationRecordRepository).save(existing);
    }

    @Test
    void updateApplicationRecordShouldNotChangeStatusUpdatedAtWhenStatusDoesNotChange() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());

        LocalDateTime originalStatusUpdatedAt = LocalDateTime.now().minusDays(3);

        ApplicationRecord existing = new ApplicationRecord(
                user, "Old Company", "Old Role", null, null, null, null,
                ApplicationStatus.APPLIED, LocalDateTime.now().minusDays(7),
                LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(3), originalStatusUpdatedAt, "Old notes"
        );

        ApplicationRecord updated = new ApplicationRecord(
                user, "Updated Company", "Updated Role", null, null, null, null,
                ApplicationStatus.APPLIED, LocalDateTime.now().minusDays(1),
                null, null, null, "Updated notes"
        );

        when(applicationRecordRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(existing));
        when(applicationRecordRepository.save(existing)).thenReturn(existing);

        ApplicationRecord result = buildServiceWithRules().updateApplicationRecord(1L, updated, user);

        assertEquals("Updated Company", result.getCompanyName());
        assertEquals("Updated Role", result.getPositionTitle());
        assertEquals(originalStatusUpdatedAt, result.getStatusUpdatedAt());
        verify(applicationRecordRepository).findByIdAndUser(1L, user);
        verify(applicationRecordRepository).save(existing);
    }

    @Test
    void deleteApplicationRecordShouldFindAndDeleteApplication() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        ApplicationRecord existing = new ApplicationRecord(
                user, "Acme", "Software Engineer", null, null, null, null,
                ApplicationStatus.APPLIED, LocalDateTime.now(), null, null, null, null
        );

        when(applicationRecordRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(existing));

        buildServiceWithRules().deleteApplicationRecord(1L, user);

        verify(applicationRecordRepository).findByIdAndUser(1L, user);
        verify(applicationRecordRepository).delete(existing);
    }

    @Test
    void getApplicationRecordResponseByIdShouldBuildResponseDto() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        LocalDateTime statusUpdatedAt = LocalDateTime.now().minusDays(15);

        ApplicationRecord record = new ApplicationRecord(
                user, "Acme", "Software Engineer", null, null, null, null,
                ApplicationStatus.APPLIED, LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(10), statusUpdatedAt, "Notes"
        );

        ApplicationRecordResponseDto responseDto = new ApplicationRecordResponseDto(
                1L, "Acme", "Software Engineer", null, null, null, null,
                ApplicationStatus.APPLIED, record.getAppliedAt(), record.getCreatedAt(), record.getUpdatedAt(),
                record.getStatusUpdatedAt(), 15, true, "MEDIUM", "Applied 15 days ago; consider following up.", "Notes"
        );

        when(applicationRecordRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(record));
        when(attentionRule.supports(ApplicationStatus.APPLIED)).thenReturn(true);
        when(attentionRule.needsAttention(anyLong())).thenReturn(true);
        when(attentionRule.getAttentionLevel()).thenReturn("MEDIUM");
        when(attentionRule.buildAttentionMessage(anyLong())).thenReturn("Applied 15 days ago; consider following up.");
        when(applicationRecordMapper.toApplicationRecordResponseDto(
                eq(record), anyLong(), eq(true), eq("MEDIUM"), eq("Applied 15 days ago; consider following up.")
        )).thenReturn(responseDto);

        ApplicationRecordResponseDto result = buildServiceWithRules().getApplicationRecordResponseById(1L, user);

        assertNotNull(result);
        assertEquals("Acme", result.getCompanyName());
        assertEquals("MEDIUM", result.getAttentionLevel());
        assertTrue(result.isNeedsAttention());
        verify(applicationRecordRepository).findByIdAndUser(1L, user);
        verify(attentionRule).supports(ApplicationStatus.APPLIED);
        verify(applicationRecordMapper).toApplicationRecordResponseDto(
                eq(record), anyLong(), eq(true), eq("MEDIUM"), eq("Applied 15 days ago; consider following up.")
        );
    }
}

