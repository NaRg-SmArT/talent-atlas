package com.mhosler.TalentAtlas.application;

import com.mhosler.TalentAtlas.user.User;
import com.mhosler.TalentAtlas.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApplicationRecordRepositoryTest {

    @Autowired
    private ApplicationRecordRepository applicationRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUser should return only application records for that user")
    void findByUserShouldReturnOnlyApplicationRecordsForThatUser() {
        User userOne = userRepository.save(
                new User("User One", "user1@example.com", "hash1", LocalDateTime.now())
        );
        User userTwo = userRepository.save(
                new User("User Two", "user2@example.com", "hash2", LocalDateTime.now())
        );

        applicationRecordRepository.save(new ApplicationRecord(
                userOne,
                "Acme",
                "Software Engineer",
                "https://acme.com/job1",
                "Jane Doe",
                "111-111-1111",
                "jane@acme.com",
                ApplicationStatus.APPLIED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                "First application"
        ));

        applicationRecordRepository.save(new ApplicationRecord(
                userOne,
                "Beta Corp",
                "Backend Developer",
                "https://beta.com/job2",
                "John Smith",
                "222-222-2222",
                "john@beta.com",
                ApplicationStatus.INTERVIEW,
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(3),
                "Second application"
        ));

        applicationRecordRepository.save(new ApplicationRecord(
                userTwo,
                "Gamma LLC",
                "Frontend Developer",
                "https://gamma.com/job3",
                "Mary Jones",
                "333-333-3333",
                "mary@gamma.com",
                ApplicationStatus.SAVED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1),
                "Other user's application"
        ));

        List<ApplicationRecord> results = applicationRecordRepository.findByUser(userOne);

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(record -> record.getCompanyName().equals("Acme")));
        assertTrue(results.stream().anyMatch(record -> record.getCompanyName().equals("Beta Corp")));
        assertFalse(results.stream().anyMatch(record -> record.getCompanyName().equals("Gamma LLC")));
    }

    @Test
    @DisplayName("findByIdAndUser should return application record when owned by user")
    void findByIdAndUserShouldReturnApplicationRecordWhenOwnedByUser() {
        User user = userRepository.save(
                new User("Test User", "test@example.com", "hash", LocalDateTime.now())
        );

        ApplicationRecord savedRecord = applicationRecordRepository.save(new ApplicationRecord(
                user,
                "Acme",
                "Software Engineer",
                "https://acme.com/job1",
                "Jane Doe",
                "111-111-1111",
                "jane@acme.com",
                ApplicationStatus.APPLIED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                "Application notes"
        ));

        Optional<ApplicationRecord> result =
                applicationRecordRepository.findByIdAndUser(savedRecord.getId(), user);

        assertTrue(result.isPresent());
        assertEquals("Acme", result.get().getCompanyName());
        assertEquals("Software Engineer", result.get().getPositionTitle());
        assertEquals(ApplicationStatus.APPLIED, result.get().getStatus());
    }

    @Test
    @DisplayName("findByIdAndUser should return empty when application record does not belong to user")
    void findByIdAndUserShouldReturnEmptyWhenApplicationRecordDoesNotBelongToUser() {
        User owner = userRepository.save(
                new User("Owner User", "owner@example.com", "hash1", LocalDateTime.now())
        );
        User otherUser = userRepository.save(
                new User("Other User", "other@example.com", "hash2", LocalDateTime.now())
        );

        ApplicationRecord savedRecord = applicationRecordRepository.save(new ApplicationRecord(
                owner,
                "Acme",
                "Software Engineer",
                "https://acme.com/job1",
                "Jane Doe",
                "111-111-1111",
                "jane@acme.com",
                ApplicationStatus.APPLIED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5),
                "Private application"
        ));

        Optional<ApplicationRecord> result =
                applicationRecordRepository.findByIdAndUser(savedRecord.getId(), otherUser);

        assertTrue(result.isEmpty());
    }
}

