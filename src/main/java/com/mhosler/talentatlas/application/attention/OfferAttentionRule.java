package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.springframework.stereotype.Component;

@Component
public class OfferAttentionRule implements ApplicationAttentionRule {

    @Override
    public boolean supports(ApplicationStatus status) {
        return status == ApplicationStatus.OFFER;
    }

    @Override
    public boolean needsAttention(long daysInCurrentStatus) {
        return true;
    }

    @Override
    public String getAttentionLevel() {
        return "HIGH";
    }

    @Override
    public String buildAttentionMessage(long daysInCurrentStatus) {
        return "Offer received; this application needs prompt attention.";
    }
}

