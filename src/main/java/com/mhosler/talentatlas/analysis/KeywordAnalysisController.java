package com.mhosler.TalentAtlas.analysis;

import com.mhosler.TalentAtlas.skill.Skill;
import com.mhosler.TalentAtlas.skill.SkillService;
import com.mhosler.TalentAtlas.user.User;
import com.mhosler.TalentAtlas.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class KeywordAnalysisController {

    private final KeywordAnalysisService keywordAnalysisService;
    private final UserService userService;
    private final SkillService skillService;

    public KeywordAnalysisController(
            KeywordAnalysisService keywordAnalysisService,
            UserService userService,
            SkillService skillService
    ) {
        this.keywordAnalysisService = keywordAnalysisService;
        this.userService = userService;
        this.skillService = skillService;
    }

    @PostMapping("/keywords")
    public ResponseEntity<KeywordAnalysisResponseDto> analyzeKeywords(
            @Valid @RequestBody KeywordAnalysisRequestDto requestDto,
            Principal principal
    ) {
        User user = userService.findEntityByEmail(principal.getName());
        List<Skill> skills = skillService.findAllByUser(user);

        KeywordAnalysisResponseDto response = keywordAnalysisService.analyze(
                skills,
                requestDto.getJobDescriptionText()
        );

        return ResponseEntity.ok(response);
    }
}

