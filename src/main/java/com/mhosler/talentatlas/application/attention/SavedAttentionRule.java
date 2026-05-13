package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.springframework.stereotype.Component;

@Component
public class SavedAttentionRule implements ApplicationAttentionRule {

    @Override
    public boolean supports(ApplicationStatus status) {
        return status == ApplicationStatus.SAVED;
    }

    @Override
    public boolean needsAttention(long daysInCurrentStatus) {
        return daysInCurrentStatus > 4;
    }

    @Override
    public String getAttentionLevel() {
        return "LOW";
    }

    @Override
    public String buildAttentionMessage(long daysInCurrentStatus) {
        return "Saved " + daysInCurrentStatus + " days ago; apply or archive.";
    }
}

