package com.mhosler.TalentAtlas.analysis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class KeywordAnalysisRequestDto {

    @NotBlank(message = "Job description text is required")
    @Size(max = 10000, message = "Job description text must be 10000 characters or fewer")
    private String jobDescriptionText;

    public KeywordAnalysisRequestDto() {
    }

    public KeywordAnalysisRequestDto(String jobDescriptionText) {
        this.jobDescriptionText = jobDescriptionText;
    }

    public String getJobDescriptionText() {
        return jobDescriptionText;
    }

    public void setJobDescriptionText(String jobDescriptionText) {
        this.jobDescriptionText = jobDescriptionText;
    }
}

