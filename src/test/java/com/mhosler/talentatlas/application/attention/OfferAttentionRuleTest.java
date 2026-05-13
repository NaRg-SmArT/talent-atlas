package com.mhosler.TalentAtlas.application.attention;

import com.mhosler.TalentAtlas.application.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OfferAttentionRule.
 *
 * Tests the business logic that determines when an OFFER application
 * needs user attention. OFFER status always requires immediate attention.
 */
class OfferAttentionRuleTest {

    private OfferAttentionRule rule;

    @BeforeEach
    void setUp() {
        rule = new OfferAttentionRule();
    }

    @Test
    void supports_shouldReturnTrue_whenStatusIsOffer() {
        boolean result = rule.supports(ApplicationStatus.OFFER);
        assertTrue(result, "OfferAttentionRule should support OFFER status");
    }

    @Test
    void supports_shouldReturnFalse_whenStatusIsNotOffer() {
        ApplicationStatus[] otherStatuses = {
                ApplicationStatus.SAVED,
                ApplicationStatus.APPLIED,
                ApplicationStatus.SCREENING,
                ApplicationStatus.INTERVIEW,
                ApplicationStatus.ACCEPTED,
                ApplicationStatus.REJECTED,
                ApplicationStatus.WITHDRAWN
        };

        for (ApplicationStatus status : otherStatuses) {
            assertFalse(rule.supports(status),
                    "OfferAttentionRule should not support " + status + " status");
        }
    }

    @Test
    void needsAttention_shouldAlwaysReturnTrue_regardlessOfDays() {
        long[] testDays = {0, 1, 5, 10, 100};

        for (long days : testDays) {
            assertTrue(rule.needsAttention(days),
                    "OfferAttentionRule should always need attention, even at " + days + " days");
        }
    }

    @Test
    void getAttentionLevel_shouldReturnHigh() {
        String level = rule.getAttentionLevel();
        assertEquals("HIGH", level, "OfferAttentionRule should return HIGH attention level");
    }

    @Test
    void buildAttentionMessage_shouldIndicateUrgency() {
        String message = rule.buildAttentionMessage(1);
        String lowerMessage = message.toLowerCase();

        assertTrue(
                lowerMessage.contains("offer") || lowerMessage.contains("prompt") || lowerMessage.contains("attention"),
                "Attention message should convey urgency about the offer"
        );
    }
}

