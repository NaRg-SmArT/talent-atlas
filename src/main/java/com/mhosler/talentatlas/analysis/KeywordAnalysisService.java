package com.mhosler.TalentAtlas.analysis;

import com.mhosler.TalentAtlas.skill.Skill;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class KeywordAnalysisService {

    private final KeywordDictionaryService keywordDictionaryService;

    public KeywordAnalysisService(KeywordDictionaryService keywordDictionaryService) {
        this.keywordDictionaryService = keywordDictionaryService;
    }

    public KeywordAnalysisResponseDto analyze(List<Skill> skills, String jobDescriptionText) {
        List<KeywordDictionaryEntry> allKeywords = keywordDictionaryService.loadDictionary();

        String normalizedJobText = normalize(jobDescriptionText);

        List<KeywordMatchResultDto> matched = new ArrayList<>();
        List<KeywordMatchResultDto> missing = new ArrayList<>();

        Set<String> userSkillTexts = buildNormalizedSkillTexts(skills);

        for (KeywordDictionaryEntry entry : allKeywords) {
            String jobMatchedAlias = findFirstMatchingAlias(normalizedJobText, entry);

            if (jobMatchedAlias == null) {
                continue;
            }

            String userMatchedAlias = findMatchingAliasInSkills(userSkillTexts, entry);

            if (userMatchedAlias != null) {
                matched.add(new KeywordMatchResultDto(
                        entry.getName(),
                        entry.getCategory(),
                        true,
                        userMatchedAlias
                ));
            } else {
                missing.add(new KeywordMatchResultDto(
                        entry.getName(),
                        entry.getCategory(),
                        false,
                        jobMatchedAlias
                ));
            }
        }

        int totalKeywords = matched.size() + missing.size();

        return new KeywordAnalysisResponseDto(
                totalKeywords,
                matched.size(),
                missing.size(),
                matched,
                missing
        );
    }

    private Set<String> buildNormalizedSkillTexts(List<Skill> skills) {
        Set<String> normalizedSkills = new LinkedHashSet<>();

        for (Skill skill : skills) {
            if (skill.getName() != null && !skill.getName().isBlank()) {
                normalizedSkills.add(normalize(skill.getName()));
            }
        }

        return normalizedSkills;
    }

    private String findMatchingAliasInSkills(Set<String> normalizedSkillTexts, KeywordDictionaryEntry entry) {
        List<String> aliasesToCheck = buildAliasesToCheck(entry);

        for (String skillText : normalizedSkillTexts) {
            for (String alias : aliasesToCheck) {
                if (skillText.contains(alias) || alias.contains(skillText)) {
                    return alias;
                }
            }
        }

        return null;
    }

    private String findFirstMatchingAlias(String normalizedText, KeywordDictionaryEntry entry) {
        for (String alias : buildAliasesToCheck(entry)) {
            if (normalizedText.contains(alias)) {
                return alias;
            }
        }

        return null;
    }

    private List<String> buildAliasesToCheck(KeywordDictionaryEntry entry) {
        List<String> aliases = new ArrayList<>();
        aliases.add(normalize(entry.getName()));

        if (entry.getAliases() != null) {
            for (String alias : entry.getAliases()) {
                aliases.add(normalize(alias));
            }
        }

        return aliases;
    }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT).trim();
    }
}

