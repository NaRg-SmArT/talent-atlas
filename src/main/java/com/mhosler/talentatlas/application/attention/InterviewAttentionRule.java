package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.springframework.stereotype.Component;

@Component
public class InterviewAttentionRule implements ApplicationAttentionRule {

    @Override
    public boolean supports(ApplicationStatus status) {
        return status == ApplicationStatus.INTERVIEW;
    }

    @Override
    public boolean needsAttention(long daysInCurrentStatus) {
        return daysInCurrentStatus >= 7;
    }

    @Override
    public String getAttentionLevel() {
        return "MEDIUM";
    }

    @Override
    public String buildAttentionMessage(long daysInCurrentStatus) {
        return "Interview stage for " + daysInCurrentStatus + " days; follow up or prepare next steps.";
    }
}

