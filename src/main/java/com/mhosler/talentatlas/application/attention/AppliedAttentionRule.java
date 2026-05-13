package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.springframework.stereotype.Component;

@Component
public class AppliedAttentionRule implements ApplicationAttentionRule {

    @Override
    public boolean supports(ApplicationStatus status) {
        return status == ApplicationStatus.APPLIED;
    }

    @Override
    public boolean needsAttention(long daysInCurrentStatus) {
        return daysInCurrentStatus >= 14;
    }

    @Override
    public String getAttentionLevel() {
        return "MEDIUM";
    }

    @Override
    public String buildAttentionMessage(long daysInCurrentStatus) {
        return "Applied " + daysInCurrentStatus + " days ago; consider following up.";
    }
}

