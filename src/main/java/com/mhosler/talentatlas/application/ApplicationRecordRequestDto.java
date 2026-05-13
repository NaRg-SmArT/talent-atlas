package com.mhosler.TalentAtlas.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ApplicationRecordRequestDto {

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name must be 100 characters or fewer")
    private String companyName;

    @NotBlank(message = "Position title is required")
    @Size(max = 100, message = "Position title must be 100 characters or fewer")
    private String positionTitle;

    @Size(max = 500, message = "Job posting URL must be 500 characters or fewer")
    private String jobPostingUrl;

    @Size(max = 100, message = "Primary contact name must be 100 characters or fewer")
    private String primaryContactName;

    @Size(max = 30, message = "Primary contact phone must be 30 characters or fewer")
    private String primaryContactPhone;

    @Email(message = "Primary contact email must be a valid email address")
    @Size(max = 150, message = "Primary contact email must be 150 characters or fewer")
    private String primaryContactEmail;

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    @NotNull(message = "Applied date is required")
    private LocalDateTime appliedAt;

    @Size(max = 2000, message = "Notes must be 2000 characters or fewer")
    private String notes;

    public ApplicationRecordRequestDto() {
    }

    public ApplicationRecordRequestDto(
            String companyName,
            String positionTitle,
            String jobPostingUrl,
            String primaryContactName,
            String primaryContactPhone,
            String primaryContactEmail,
            ApplicationStatus status,
            LocalDateTime appliedAt,
            String notes
    ) {
        this.companyName = companyName;
        this.positionTitle = positionTitle;
        this.jobPostingUrl = jobPostingUrl;
        this.primaryContactName = primaryContactName;
        this.primaryContactPhone = primaryContactPhone;
        this.primaryContactEmail = primaryContactEmail;
        this.status = status;
        this.appliedAt = appliedAt;
        this.notes = notes;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public String getJobPostingUrl() {
        return jobPostingUrl;
    }

    public void setJobPostingUrl(String jobPostingUrl) {
        this.jobPostingUrl = jobPostingUrl;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    public String getPrimaryContactPhone() {
        return primaryContactPhone;
    }

    public void setPrimaryContactPhone(String primaryContactPhone) {
        this.primaryContactPhone = primaryContactPhone;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public void setPrimaryContactEmail(String primaryContactEmail) {
        this.primaryContactEmail = primaryContactEmail;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

