package com.mhosler.TalentAtlas.application;

import com.mhosler.TalentAtlas.user.User;
import com.mhosler.TalentAtlas.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationRecordController {

    private final ApplicationRecordService applicationRecordService;
    private final ApplicationRecordMapper applicationRecordMapper;
    private final UserService userService;

    public ApplicationRecordController(
            ApplicationRecordService applicationRecordService,
            ApplicationRecordMapper applicationRecordMapper,
            UserService userService
    ) {
        this.applicationRecordService = applicationRecordService;
        this.applicationRecordMapper = applicationRecordMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationRecordResponseDto>> getAllApplicationRecords(Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        return ResponseEntity.ok(applicationRecordService.getApplicationRecordResponsesByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationRecordResponseDto> getApplicationRecordById(
            @PathVariable Long id,
            Principal principal
    ) {
        User user = userService.findEntityByEmail(principal.getName());
        return ResponseEntity.ok(applicationRecordService.getApplicationRecordResponseById(id, user));
    }

    @PostMapping
    public ResponseEntity<ApplicationRecordResponseDto> createApplicationRecord(
            @Valid @RequestBody ApplicationRecordRequestDto requestDto,
            Principal principal
    ) {
        User user = userService.findEntityByEmail(principal.getName());
        ApplicationRecord applicationRecord = applicationRecordMapper.toApplicationRecord(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationRecordService.addApplicationRecordAndReturnResponse(applicationRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationRecordResponseDto> updateApplicationRecord(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRecordRequestDto requestDto,
            Principal principal
    ) {
        User user = userService.findEntityByEmail(principal.getName());
        ApplicationRecord applicationRecord = applicationRecordMapper.toApplicationRecord(requestDto, user);
        return ResponseEntity.ok(
                applicationRecordService.updateApplicationRecordAndReturnResponse(id, applicationRecord, user)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationRecord(@PathVariable Long id, Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        applicationRecordService.deleteApplicationRecord(id, user);
        return ResponseEntity.noContent().build();
    }
}

