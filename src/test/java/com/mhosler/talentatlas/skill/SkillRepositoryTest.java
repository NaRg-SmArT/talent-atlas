package com.mhosler.TalentAtlas.skill;

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
class SkillRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUser should return only skills for that user")
    void findByUserShouldReturnOnlySkillsForThatUser() {
        User userOne = userRepository.save(
                new User("User One", "user1@example.com", "hash1", LocalDateTime.now())
        );
        User userTwo = userRepository.save(
                new User("User Two", "user2@example.com", "hash2", LocalDateTime.now())
        );

        skillRepository.save(new Skill(userOne, "Java", "Programming Language", 5));
        skillRepository.save(new Skill(userOne, "Spring Boot", "Framework", 4));
        skillRepository.save(new Skill(userTwo, "Python", "Programming Language", 3));

        List<Skill> results = skillRepository.findByUser(userOne);

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(skill -> skill.getName().equals("Java")));
        assertTrue(results.stream().anyMatch(skill -> skill.getName().equals("Spring Boot")));
        assertFalse(results.stream().anyMatch(skill -> skill.getName().equals("Python")));
    }

    @Test
    @DisplayName("findByIdAndUser should return skill when owned by user")
    void findByIdAndUserShouldReturnSkillWhenOwnedByUser() {
        User user = userRepository.save(
                new User("Test User", "test@example.com", "hash", LocalDateTime.now())
        );

        Skill savedSkill = skillRepository.save(
                new Skill(user, "Java", "Programming Language", 5)
        );

        Optional<Skill> result = skillRepository.findByIdAndUser(savedSkill.getId(), user);

        assertTrue(result.isPresent());
        assertEquals("Java", result.get().getName());
        assertEquals("Programming Language", result.get().getCategory());
        assertEquals(5, result.get().getProficiency());
    }

    @Test
    @DisplayName("findByIdAndUser should return empty when skill does not belong to user")
    void findByIdAndUserShouldReturnEmptyWhenSkillDoesNotBelongToUser() {
        User owner = userRepository.save(
                new User("Owner User", "owner@example.com", "hash1", LocalDateTime.now())
        );
        User otherUser = userRepository.save(
                new User("Other User", "other@example.com", "hash2", LocalDateTime.now())
        );

        Skill savedSkill = skillRepository.save(
                new Skill(owner, "Java", "Programming Language", 5)
        );

        Optional<Skill> result = skillRepository.findByIdAndUser(savedSkill.getId(), otherUser);

        assertTrue(result.isEmpty());
    }
}

