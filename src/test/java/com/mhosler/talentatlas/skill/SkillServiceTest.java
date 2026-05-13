package com.mhosler.TalentAtlas.skill;

import com.mhosler.TalentAtlas.exception.ResourceNotFoundException;
import com.mhosler.TalentAtlas.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    void findAllByUserShouldReturnSkillsForUser() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        List<Skill> expectedSkills = List.of(
                new Skill(user, "Java", "Hard Skill", 4),
                new Skill(user, "Communication", "Soft Skill", 5)
        );

        when(skillRepository.findByUser(user)).thenReturn(expectedSkills);

        List<Skill> actualSkills = skillService.findAllByUser(user);

        assertEquals(expectedSkills, actualSkills);
        verify(skillRepository).findByUser(user);
    }

    @Test
    void findByIdAndUserShouldReturnSkillWhenFound() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        Skill skill = new Skill(user, "Java", "Hard Skill", 4);

        when(skillRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(skill));

        Skill result = skillService.findByIdAndUser(1L, user);

        assertNotNull(result);
        assertEquals("Java", result.getName());
        assertEquals("Hard Skill", result.getCategory());
        assertEquals(4, result.getProficiency());
        verify(skillRepository).findByIdAndUser(1L, user);
    }

    @Test
    void findByIdAndUserShouldThrowResourceNotFoundExceptionWhenSkillNotFound() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());

        when(skillRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.findByIdAndUser(1L, user)
        );

        assertEquals("Skill not found: 1", exception.getMessage());
        verify(skillRepository).findByIdAndUser(1L, user);
    }

    @Test
    void addSkillShouldSaveAndReturnSkill() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        Skill skill = new Skill(user, "Spring Boot", "Hard Skill", 5);

        when(skillRepository.save(skill)).thenReturn(skill);

        Skill savedSkill = skillService.addSkill(skill);

        assertNotNull(savedSkill);
        assertEquals("Spring Boot", savedSkill.getName());
        assertEquals("Hard Skill", savedSkill.getCategory());
        assertEquals(5, savedSkill.getProficiency());
        verify(skillRepository).save(skill);
    }

    @Test
    void updateSkillShouldUpdateFieldsAndSaveSkill() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        Skill existingSkill = new Skill(user, "Java", "Hard Skill", 3);
        Skill updatedSkill = new Skill(user, "Java", "Hard Skill", 5);

        when(skillRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(existingSkill));
        when(skillRepository.save(existingSkill)).thenReturn(existingSkill);

        Skill result = skillService.updateSkill(1L, updatedSkill, user);

        assertNotNull(result);
        assertEquals("Java", result.getName());
        assertEquals("Hard Skill", result.getCategory());
        assertEquals(5, result.getProficiency());
        verify(skillRepository).findByIdAndUser(1L, user);
        verify(skillRepository).save(existingSkill);
    }

    @Test
    void deleteSkillShouldFindAndDeleteSkill() {
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
        Skill skill = new Skill(user, "SQL", "Hard Skill", 4);

        when(skillRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(skill));

        skillService.deleteSkill(1L, user);

        verify(skillRepository).findByIdAndUser(1L, user);
        verify(skillRepository).delete(skill);
    }
}

