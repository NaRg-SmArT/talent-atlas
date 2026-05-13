package com.mhosler.TalentAtlas.skill;

import com.mhosler.TalentAtlas.exception.ResourceNotFoundException;
import com.mhosler.TalentAtlas.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> findAllByUser(User user) {
        return skillRepository.findByUser(user);
    }

    public Skill findByIdAndUser(Long id, User user) {
        return skillRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + id));
    }

    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Long id, Skill updatedSkill, User user) {
        Skill existingSkill = findByIdAndUser(id, user);
        existingSkill.setName(updatedSkill.getName());
        existingSkill.setCategory(updatedSkill.getCategory());
        existingSkill.setProficiency(updatedSkill.getProficiency());
        return skillRepository.save(existingSkill);
    }

    public void deleteSkill(Long id, User user) {
        Skill existingSkill = findByIdAndUser(id, user);
        skillRepository.delete(existingSkill);
    }
}

