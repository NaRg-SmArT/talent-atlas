package com.mhosler.TalentAtlas.application;

import com.mhosler.TalentAtlas.application.attention.ApplicationAttentionRule;
import com.mhosler.TalentAtlas.exception.ResourceNotFoundException;
import com.mhosler.TalentAtlas.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ApplicationRecordService {

    private final ApplicationRecordRepository applicationRecordRepository;
    private final ApplicationRecordMapper applicationRecordMapper;
    private final List<ApplicationAttentionRule> attentionRules;

    public ApplicationRecordService(
            ApplicationRecordRepository applicationRecordRepository,
            ApplicationRecordMapper applicationRecordMapper,
            List<ApplicationAttentionRule> attentionRules
    ) {
        this.applicationRecordRepository = applicationRecordRepository;
        this.applicationRecordMapper = applicationRecordMapper;
        this.attentionRules = attentionRules;
    }

    public List<ApplicationRecord> findAllByUser(User user) {
        return applicationRecordRepository.findByUser(user);
    }

    public List<ApplicationRecordResponseDto> getApplicationRecordResponsesByUser(User user) {
        return findAllByUser(user).stream()
                .map(this::buildResponseDto)
                .toList();
    }

    public ApplicationRecord findByIdAndUser(Long id, User user) {
        return applicationRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + id));
    }

    public ApplicationRecordResponseDto getApplicationRecordResponseById(Long id, User user) {
        return buildResponseDto(findByIdAndUser(id, user));
    }

    public ApplicationRecord addApplicationRecord(ApplicationRecord applicationRecord) {
        LocalDateTime now = LocalDateTime.now();
        applicationRecord.setCreatedAt(now);
        applicationRecord.setUpdatedAt(now);
        applicationRecord.setStatusUpdatedAt(now);
        return applicationRecordRepository.save(applicationRecord);
    }

    public ApplicationRecordResponseDto addApplicationRecordAndReturnResponse(ApplicationRecord applicationRecord) {
        ApplicationRecord savedRecord = addApplicationRecord(applicationRecord);
        return buildResponseDto(savedRecord);
    }

    public ApplicationRecord updateApplicationRecord(Long id, ApplicationRecord updatedApplicationRecord, User user) {
        ApplicationRecord existingRecord = findByIdAndUser(id, user);
        boolean statusChanged = existingRecord.getStatus() != updatedApplicationRecord.getStatus();
        LocalDateTime now = LocalDateTime.now();

        existingRecord.setCompanyName(updatedApplicationRecord.getCompanyName());
        existingRecord.setPositionTitle(updatedApplicationRecord.getPositionTitle());
        existingRecord.setJobPostingUrl(updatedApplicationRecord.getJobPostingUrl());
        existingRecord.setPrimaryContactName(updatedApplicationRecord.getPrimaryContactName());
        existingRecord.setPrimaryContactPhone(updatedApplicationRecord.getPrimaryContactPhone());
        existingRecord.setPrimaryContactEmail(updatedApplicationRecord.getPrimaryContactEmail());
        existingRecord.setStatus(updatedApplicationRecord.getStatus());
        existingRecord.setAppliedAt(updatedApplicationRecord.getAppliedAt());
        existingRecord.setNotes(updatedApplicationRecord.getNotes());
        existingRecord.setUpdatedAt(now);

        if (statusChanged) {
            existingRecord.setStatusUpdatedAt(now);
        }

        return applicationRecordRepository.save(existingRecord);
    }

    public ApplicationRecordResponseDto updateApplicationRecordAndReturnResponse(
            Long id,
            ApplicationRecord updatedApplicationRecord,
            User user
    ) {
        ApplicationRecord savedRecord = updateApplicationRecord(id, updatedApplicationRecord, user);
        return buildResponseDto(savedRecord);
    }

    public void deleteApplicationRecord(Long id, User user) {
        ApplicationRecord existingRecord = findByIdAndUser(id, user);
        applicationRecordRepository.delete(existingRecord);
    }

    private ApplicationRecordResponseDto buildResponseDto(ApplicationRecord applicationRecord) {
        long daysInCurrentStatus = getDaysInCurrentStatus(applicationRecord);
        ApplicationAttentionRule rule = getAttentionRule(applicationRecord.getStatus());

        boolean needsAttention = rule.needsAttention(daysInCurrentStatus);
        String attentionLevel = needsAttention ? rule.getAttentionLevel() : "NONE";
        String attentionMessage = needsAttention ? rule.buildAttentionMessage(daysInCurrentStatus) : "";

        return applicationRecordMapper.toApplicationRecordResponseDto(
                applicationRecord,
                daysInCurrentStatus,
                needsAttention,
                attentionLevel,
                attentionMessage
        );
    }

    private long getDaysInCurrentStatus(ApplicationRecord applicationRecord) {
        LocalDateTime statusUpdatedAt = applicationRecord.getStatusUpdatedAt();

        if (statusUpdatedAt == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(statusUpdatedAt.toLocalDate(), LocalDate.now());
    }

    private ApplicationAttentionRule getAttentionRule(ApplicationStatus status) {
        return attentionRules.stream()
                .filter(rule -> rule.supports(status))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No attention rule found for status: " + status));
    }
}
