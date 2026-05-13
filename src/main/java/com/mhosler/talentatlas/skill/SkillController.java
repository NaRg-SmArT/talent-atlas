package com.mhosler.TalentAtlas.skill;

import com.mhosler.TalentAtlas.user.User;
import com.mhosler.TalentAtlas.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;
    private final SkillMapper skillMapper;
    private final UserService userService;

    public SkillController(SkillService skillService, SkillMapper skillMapper, UserService userService) {
        this.skillService = skillService;
        this.skillMapper = skillMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<SkillResponseDto>> getAllSkills(Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        List<SkillResponseDto> skills = skillService.findAllByUser(user)
                .stream()
                .map(skillMapper::toSkillResponseDto)
                .toList();
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDto> getSkillById(@PathVariable Long id, Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        Skill skill = skillService.findByIdAndUser(id, user);
        return ResponseEntity.ok(skillMapper.toSkillResponseDto(skill));
    }

    @PostMapping
    public ResponseEntity<SkillResponseDto> createSkill(
            @Valid @RequestBody SkillRequestDto skillRequestDto,
            Principal principal
    ) {
        User user = userService.findEntityByEmail(principal.getName());
        Skill skill = skillMapper.toSkill(skillRequestDto, user);
        Skill savedSkill = skillService.addSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(skillMapper.toSkillResponseDto(savedSkill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponseDto> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequestDto skillRequestDto,
            Principal principal
    ) {
        User user = userService.findEntityByEmail(principal.getName());
        Skill skill = skillMapper.toSkill(skillRequestDto, user);
        Skill updatedSkill = skillService.updateSkill(id, skill, user);
        return ResponseEntity.ok(skillMapper.toSkillResponseDto(updatedSkill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id, Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        skillService.deleteSkill(id, user);
        return ResponseEntity.noContent().build();
    }
}

