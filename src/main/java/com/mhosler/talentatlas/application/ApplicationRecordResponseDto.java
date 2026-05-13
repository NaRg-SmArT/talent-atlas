package com.mhosler.TalentAtlas.application;

import java.time.LocalDateTime;

public class ApplicationRecordResponseDto {

    private Long id;
    private String companyName;
    private String positionTitle;
    private String jobPostingUrl;
    private String primaryContactName;
    private String primaryContactPhone;
    private String primaryContactEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime statusUpdatedAt;
    private long daysInCurrentStatus;
    private boolean needsAttention;
    private String attentionLevel;
    private String attentionMessage;
    private String notes;

    public ApplicationRecordResponseDto(
            Long id,
            String companyName,
            String positionTitle,
            String jobPostingUrl,
            String primaryContactName,
            String primaryContactPhone,
            String primaryContactEmail,
            ApplicationStatus status,
            LocalDateTime appliedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime statusUpdatedAt,
            long daysInCurrentStatus,
            boolean needsAttention,
            String attentionLevel,
            String attentionMessage,
            String notes
    ) {
        this.id = id;
        this.companyName = companyName;
        this.positionTitle = positionTitle;
        this.jobPostingUrl = jobPostingUrl;
        this.primaryContactName = primaryContactName;
        this.primaryContactPhone = primaryContactPhone;
        this.primaryContactEmail = primaryContactEmail;
        this.status = status;
        this.appliedAt = appliedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.statusUpdatedAt = statusUpdatedAt;
        this.daysInCurrentStatus = daysInCurrentStatus;
        this.needsAttention = needsAttention;
        this.attentionLevel = attentionLevel;
        this.attentionMessage = attentionMessage;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public String getJobPostingUrl() {
        return jobPostingUrl;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public String getPrimaryContactPhone() {
        return primaryContactPhone;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public long getDaysInCurrentStatus() {
        return daysInCurrentStatus;
    }

    public boolean isNeedsAttention() {
        return needsAttention;
    }

    public String getAttentionLevel() {
        return attentionLevel;
    }

    public String getAttentionMessage() {
        return attentionMessage;
    }

    public String getNotes() {
        return notes;
    }
}

