package com.mhosler.TalentAtlas.analysis;

import java.util.List;

public class KeywordAnalysisResponseDto {

    private int totalKeywords;
    private int matchedCount;
    private int missingCount;
    private List<KeywordMatchResultDto> matched;
    private List<KeywordMatchResultDto> missing;

    public KeywordAnalysisResponseDto() {
    }

    public KeywordAnalysisResponseDto(
            int totalKeywords,
            int matchedCount,
            int missingCount,
            List<KeywordMatchResultDto> matched,
            List<KeywordMatchResultDto> missing
    ) {
        this.totalKeywords = totalKeywords;
        this.matchedCount = matchedCount;
        this.missingCount = missingCount;
        this.matched = matched;
        this.missing = missing;
    }

    public int getTotalKeywords() {
        return totalKeywords;
    }

    public void setTotalKeywords(int totalKeywords) {
        this.totalKeywords = totalKeywords;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
    }

    public int getMissingCount() {
        return missingCount;
    }

    public void setMissingCount(int missingCount) {
        this.missingCount = missingCount;
    }

    public List<KeywordMatchResultDto> getMatched() {
        return matched;
    }

    public void setMatched(List<KeywordMatchResultDto> matched) {
        this.matched = matched;
    }

    public List<KeywordMatchResultDto> getMissing() {
        return missing;
    }

    public void setMissing(List<KeywordMatchResultDto> missing) {
        this.missing = missing;
    }
}

