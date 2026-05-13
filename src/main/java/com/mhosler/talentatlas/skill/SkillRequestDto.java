package com.mhosler.TalentAtlas.skill;

import jakarta.validation.constraints.*;

public class SkillRequestDto {

    @NotBlank(message = "Skill name is required")
    @Size(max = 100, message = "Skill name must be 100 characters or fewer")
    private String name;

    @NotBlank(message = "Category is required")
    @Pattern(
            regexp = "Hard Skill|Soft Skill",
            message = "Category must be Hard Skill or Soft Skill"
    )
    private String category;

    @NotNull(message = "Proficiency is required")
    @Min(value = 1, message = "Proficiency must be from 1 to 5")
    @Max(value = 5, message = "Proficiency must be from 1 to 5")
    private Integer proficiency;

    public SkillRequestDto() {
    }

    public SkillRequestDto(String name, String category, Integer proficiency) {
        this.name = name;
        this.category = category;
        this.proficiency = proficiency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getProficiency() {
        return proficiency;
    }

    public void setProficiency(Integer proficiency) {
        this.proficiency = proficiency;
    }
}

