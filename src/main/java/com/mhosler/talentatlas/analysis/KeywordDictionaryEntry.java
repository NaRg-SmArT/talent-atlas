package com.mhosler.TalentAtlas.analysis;

import java.util.List;

public class KeywordDictionaryEntry {

    private String name;
    private String category;
    private List<String> aliases;

    public KeywordDictionaryEntry() {
    }

    public KeywordDictionaryEntry(String name, String category, List<String> aliases) {
        this.name = name;
        this.category = category;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
}

