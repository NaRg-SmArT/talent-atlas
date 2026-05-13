package com.mhosler.TalentAtlas.analysis;

import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeywordDictionaryServiceTest {

    @Test
    void loadDictionaryShouldReturnEntriesFromJsonFile() {
        KeywordDictionaryService service = new KeywordDictionaryService(new JsonMapper());

        List<KeywordDictionaryEntry> entries = service.loadDictionary();

        assertNotNull(entries);
        assertFalse(entries.isEmpty());

        KeywordDictionaryEntry firstEntry = entries.get(0);
        assertNotNull(firstEntry.getName());
        assertNotNull(firstEntry.getCategory());
        assertNotNull(firstEntry.getAliases());
    }

    @Test
    void loadDictionaryShouldContainExpectedKeyword() {
        KeywordDictionaryService service = new KeywordDictionaryService(new JsonMapper());

        List<KeywordDictionaryEntry> entries = service.loadDictionary();

        boolean foundJava = entries.stream()
                .anyMatch(entry -> "Java".equalsIgnoreCase(entry.getName()));

        assertTrue(foundJava);
    }

    @Test
    void loadDictionaryShouldThrowIllegalStateExceptionWhenMapperFails() {
        JsonMapper failingMapper = new JsonMapper() {
            @Override
            public <T> T readValue(InputStream src, TypeReference<T> valueTypeRef) {
                throw new RuntimeException("boom");
            }
        };

        KeywordDictionaryService service = new KeywordDictionaryService(failingMapper);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                service::loadDictionary
        );

        assertEquals("Failed to load keyword dictionary.", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("boom", exception.getCause().getMessage());
    }
}

