package com.mhosler.TalentAtlas.analysis;

import tools.jackson.core.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.util.List;

@Service
public class KeywordDictionaryService {

    private final JsonMapper jsonMapper;

    public KeywordDictionaryService(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public List<KeywordDictionaryEntry> loadDictionary() {
        try {
            ClassPathResource resource = new ClassPathResource("keyword-dictionary.json");

            try (InputStream inputStream = resource.getInputStream()) {
                return jsonMapper.readValue(
                        inputStream,
                        new TypeReference<List<KeywordDictionaryEntry>>() {}
                );
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load keyword dictionary.", ex);
        }
    }
}
