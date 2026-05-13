package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.springframework.stereotype.Component;

@Component
public class ClosedAttentionRule implements ApplicationAttentionRule {

    @Override
    public boolean supports(ApplicationStatus status) {
        return status == ApplicationStatus.ACCEPTED
                || status == ApplicationStatus.REJECTED
                || status == ApplicationStatus.WITHDRAWN;
    }

    @Override
    public boolean needsAttention(long daysInCurrentStatus) {
        return false;
    }

    @Override
    public String getAttentionLevel() {
        return "NONE";
    }

    @Override
    public String buildAttentionMessage(long daysInCurrentStatus) {
        return "";
    }
}
