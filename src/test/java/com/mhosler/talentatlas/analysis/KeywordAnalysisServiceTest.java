package com.mhosler.TalentAtlas.analysis;

import com.mhosler.TalentAtlas.skill.Skill;
import com.mhosler.TalentAtlas.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for KeywordAnalysisService.
 *
 * Tests the business logic that matches job description keywords
 * against a user's skill list to identify gaps.
 */
@ExtendWith(MockitoExtension.class)
class KeywordAnalysisServiceTest {

    @Mock
    private KeywordDictionaryService mockDictionaryService;

    private KeywordAnalysisService service;
    private User testUser;

    @BeforeEach
    void setUp() {
        service = new KeywordAnalysisService(mockDictionaryService);
        testUser = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());
    }

    @Test
    void analyze_shouldReturnZeroCounts_whenJobDescriptionIsEmpty() {
        when(mockDictionaryService.loadDictionary()).thenReturn(List.of());
        List<Skill> userSkills = List.of();

        KeywordAnalysisResponseDto result = service.analyze(userSkills, "");

        assertEquals(0, result.getTotalKeywords());
        assertEquals(0, result.getMatchedCount());
        assertEquals(0, result.getMissingCount());
    }

    @Test
    void analyze_shouldMatchSkill_whenUserHasExactKeyword() {
        KeywordDictionaryEntry javaEntry = new KeywordDictionaryEntry("Java", "Hard Skill", List.of("java"));
        when(mockDictionaryService.loadDictionary()).thenReturn(List.of(javaEntry));

        Skill javaSkill = new Skill(testUser, "Java", "Hard Skill", 3);
        List<Skill> userSkills = List.of(javaSkill);

        KeywordAnalysisResponseDto result = service.analyze(userSkills, "Looking for Java developer");

        assertEquals(1, result.getTotalKeywords());
        assertEquals(1, result.getMatchedCount());
        assertEquals(0, result.getMissingCount());
        assertEquals(1, result.getMatched().size());
        assertEquals("Java", result.getMatched().get(0).getKeywordName());
        assertTrue(result.getMatched().get(0).isMatchedFromSkills());
    }

    @Test
    void analyze_shouldFindMissingSkill_whenUserLacksKeyword() {
        KeywordDictionaryEntry pythonEntry = new KeywordDictionaryEntry("Python", "Hard Skill", List.of("python"));
        when(mockDictionaryService.loadDictionary()).thenReturn(List.of(pythonEntry));

        List<Skill> userSkills = List.of();

        KeywordAnalysisResponseDto result = service.analyze(userSkills, "Python experience required");

        assertEquals(1, result.getTotalKeywords());
        assertEquals(0, result.getMatchedCount());
        assertEquals(1, result.getMissingCount());
        assertEquals(1, result.getMissing().size());
        assertEquals("Python", result.getMissing().get(0).getKeywordName());
        assertFalse(result.getMissing().get(0).isMatchedFromSkills());
    }

    @Test
    void analyze_shouldMatchUsingAlias() {
        KeywordDictionaryEntry jsEntry = new KeywordDictionaryEntry(
                "JavaScript",
                "Hard Skill",
                List.of("javascript", "js")
        );
        when(mockDictionaryService.loadDictionary()).thenReturn(List.of(jsEntry));

        Skill jsSkill = new Skill(testUser, "JavaScript", "Hard Skill", 4);
        List<Skill> userSkills = List.of(jsSkill);

        KeywordAnalysisResponseDto result = service.analyze(userSkills, "Must know JS and Node");

        assertEquals(1, result.getMatchedCount());
        assertEquals("JavaScript", result.getMatched().get(0).getKeywordName());
    }

    @Test
    void analyze_shouldBeCaseInsensitive() {
        KeywordDictionaryEntry sqlEntry = new KeywordDictionaryEntry("SQL", "Hard Skill", List.of("sql"));
        when(mockDictionaryService.loadDictionary()).thenReturn(List.of(sqlEntry));

        Skill sqlSkill = new Skill(testUser, "sql", "Hard Skill", 3);
        List<Skill> userSkills = List.of(sqlSkill);

        KeywordAnalysisResponseDto result = service.analyze(userSkills, "STRONG SQL SKILLS REQUIRED");

        assertEquals(1, result.getMatchedCount());
    }

    @Test
    void analyze_shouldHandleMultipleKeywords() {
        KeywordDictionaryEntry javaEntry = new KeywordDictionaryEntry("Java", "Hard Skill", List.of("java"));
        KeywordDictionaryEntry pythonEntry = new KeywordDictionaryEntry("Python", "Hard Skill", List.of("python"));
        KeywordDictionaryEntry sqlEntry = new KeywordDictionaryEntry("SQL", "Hard Skill", List.of("sql"));

        when(mockDictionaryService.loadDictionary()).thenReturn(
                Arrays.asList(javaEntry, pythonEntry, sqlEntry)
        );

        Skill javaSkill = new Skill(testUser, "Java", "Hard Skill", 4);
        Skill sqlSkill = new Skill(testUser, "SQL", "Hard Skill", 3);
        List<Skill> userSkills = Arrays.asList(javaSkill, sqlSkill);

        KeywordAnalysisResponseDto result = service.analyze(
                userSkills,
                "Need Java and Python developer with SQL experience"
        );

        assertEquals(3, result.getTotalKeywords());
        assertEquals(2, result.getMatchedCount());
        assertEquals(1, result.getMissingCount());
    }

    @Test
    void analyze_shouldIgnoreKeywordsNotInJobDescription() {
        KeywordDictionaryEntry javaEntry = new KeywordDictionaryEntry("Java", "Hard Skill", List.of("java"));
        KeywordDictionaryEntry pythonEntry = new KeywordDictionaryEntry("Python", "Hard Skill", List.of("python"));

        when(mockDictionaryService.loadDictionary()).thenReturn(
                Arrays.asList(javaEntry, pythonEntry)
        );

        Skill javaSkill = new Skill(testUser, "Java", "Hard Skill", 4);
        List<Skill> userSkills = List.of(javaSkill);

        KeywordAnalysisResponseDto result = service.analyze(userSkills, "Looking for Java developer");

        assertEquals(1, result.getTotalKeywords(), "Should only count Java, not Python");
        assertEquals(1, result.getMatchedCount());
    }
}
