package com.mhosler.TalentAtlas.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByEmail should return user when email exists")
    void findByEmailShouldReturnUserWhenEmailExists() {
        User user = new User(
                "Test User",
                "test@example.com",
                "hashedPassword",
                LocalDateTime.now()
        );
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getFullName());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("findByEmail should return empty when email does not exist")
    void findByEmailShouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<User> result = userRepository.findByEmail("missing@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("existsByEmail should return true when email exists")
    void existsByEmailShouldReturnTrueWhenEmailExists() {
        User user = new User(
                "Test User",
                "test@example.com",
                "hashedPassword",
                LocalDateTime.now()
        );
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByEmail should return false when email does not exist")
    void existsByEmailShouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("missing@example.com");

        assertFalse(exists);
    }
}
