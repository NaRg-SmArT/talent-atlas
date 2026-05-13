package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;

public interface ApplicationAttentionRule {

    boolean supports(ApplicationStatus status);

    boolean needsAttention(long daysInCurrentStatus);

    String getAttentionLevel();

    String buildAttentionMessage(long daysInCurrentStatus);
}

