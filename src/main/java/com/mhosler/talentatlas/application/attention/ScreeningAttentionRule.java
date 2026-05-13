package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.springframework.stereotype.Component;

@Component
public class ScreeningAttentionRule implements ApplicationAttentionRule {

    @Override
    public boolean supports(ApplicationStatus status) {
        return status == ApplicationStatus.SCREENING;
    }

    @Override
    public boolean needsAttention(long daysInCurrentStatus) {
        return daysInCurrentStatus >= 10;
    }

    @Override
    public String getAttentionLevel() {
        return "MEDIUM";
    }

    @Override
    public String buildAttentionMessage(long daysInCurrentStatus) {
        return "In screening for " + daysInCurrentStatus + " days; check status.";
    }
}
