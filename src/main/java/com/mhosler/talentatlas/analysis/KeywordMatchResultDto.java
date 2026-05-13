package com.mhosler.TalentAtlas.analysis;

public class KeywordMatchResultDto {

    private String keywordName;
    private String category;
    private boolean matchedFromSkills;
    private String matchedAlias;

    public KeywordMatchResultDto() {
    }

    public KeywordMatchResultDto(String keywordName, String category, boolean matchedFromSkills, String matchedAlias) {
        this.keywordName = keywordName;
        this.category = category;
        this.matchedFromSkills = matchedFromSkills;
        this.matchedAlias = matchedAlias;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isMatchedFromSkills() {
        return matchedFromSkills;
    }

    public void setMatchedFromSkills(boolean matchedFromSkills) {
        this.matchedFromSkills = matchedFromSkills;
    }

    public String getMatchedAlias() {
        return matchedAlias;
    }

    public void setMatchedAlias(String matchedAlias) {
        this.matchedAlias = matchedAlias;
    }
}

