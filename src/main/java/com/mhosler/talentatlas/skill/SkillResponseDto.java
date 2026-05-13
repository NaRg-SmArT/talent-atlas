package com.mhosler.TalentAtlas.skill;

public class SkillResponseDto {
    private Long id;
    private String name;
    private String category;
    private Integer proficiency;

    public SkillResponseDto(Long id, String name, String category, Integer proficiency) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.proficiency = proficiency;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Integer getProficiency() {
        return proficiency;
    }

}

