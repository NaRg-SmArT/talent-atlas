package com.mhosler.TalentAtlas.skill;

import com.mhosler.TalentAtlas.user.User;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillResponseDto toSkillResponseDto(Skill skill) {
        return new SkillResponseDto(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                skill.getProficiency()
        );
    }

    public Skill toSkill(SkillRequestDto skillRequestDto, User user) {
        return new Skill(
                user,
                skillRequestDto.getName(),
                skillRequestDto.getCategory(),
                skillRequestDto.getProficiency()
        );
    }
}

