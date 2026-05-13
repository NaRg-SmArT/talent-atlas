package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SavedAttentionRule.
 *
 * Tests the business logic that determines when a SAVED application
 * needs user attention based on how long it has been in that status.
 */
class SavedAttentionRuleTest {

    private SavedAttentionRule rule;

    @BeforeEach
    void setUp() {
        rule = new SavedAttentionRule();
    }

    @Test
    void supports_shouldReturnTrue_whenStatusIsSaved() {
        boolean result = rule.supports(ApplicationStatus.SAVED);
        assertTrue(result, "SavedAttentionRule should support SAVED status");
    }

    @Test
    void supports_shouldReturnFalse_whenStatusIsNotSaved() {
        ApplicationStatus[] otherStatuses = {
                ApplicationStatus.APPLIED,
                ApplicationStatus.SCREENING,
                ApplicationStatus.INTERVIEW,
                ApplicationStatus.OFFER,
                ApplicationStatus.ACCEPTED,
                ApplicationStatus.REJECTED,
                ApplicationStatus.WITHDRAWN
        };

        for (ApplicationStatus status : otherStatuses) {
            assertFalse(rule.supports(status),
                    "SavedAttentionRule should not support " + status + " status");
        }
    }

    @Test
    void needsAttention_shouldReturnFalse_whenDaysIsZero() {
        boolean result = rule.needsAttention(0);
        assertFalse(result, "Should not need attention on day 0");
    }

    @Test
    void needsAttention_shouldReturnFalse_whenDaysIsFour() {
        boolean result = rule.needsAttention(4);
        assertFalse(result, "Should not need attention exactly at 4 days");
    }

    @Test
    void needsAttention_shouldReturnTrue_whenDaysIsFive() {
        boolean result = rule.needsAttention(5);
        assertTrue(result, "Should need attention at 5 days (threshold is >4)");
    }

    @Test
    void needsAttention_shouldReturnTrue_whenDaysIsVeryHigh() {
        boolean result = rule.needsAttention(100);
        assertTrue(result, "Should need attention when days is much greater than threshold");
    }

    @Test
    void getAttentionLevel_shouldReturnLow() {
        String level = rule.getAttentionLevel();
        assertEquals("LOW", level, "SavedAttentionRule should return LOW attention level");
    }

    @Test
    void buildAttentionMessage_shouldIncludeDaysCount() {
        String message = rule.buildAttentionMessage(7);
        assertTrue(message.contains("7"), "Attention message should include the number of days");
    }

    @Test
    void buildAttentionMessage_shouldIndicateActionRequired() {
        String message = rule.buildAttentionMessage(5);
        assertTrue(message.toLowerCase().contains("apply") || message.toLowerCase().contains("archive"),
                "Attention message should suggest applying or archiving");
    }
}
