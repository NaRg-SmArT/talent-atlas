package com.mhosler.TalentAtlas.application;

import com.mhosler.TalentAtlas.user.User;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRecordMapper {

    public ApplicationRecordResponseDto toApplicationRecordResponseDto(
            ApplicationRecord applicationRecord,
            long daysInCurrentStatus,
            boolean needsAttention,
            String attentionLevel,
            String attentionMessage
    ) {
        return new ApplicationRecordResponseDto(
                applicationRecord.getId(),
                applicationRecord.getCompanyName(),
                applicationRecord.getPositionTitle(),
                applicationRecord.getJobPostingUrl(),
                applicationRecord.getPrimaryContactName(),
                applicationRecord.getPrimaryContactPhone(),
                applicationRecord.getPrimaryContactEmail(),
                applicationRecord.getStatus(),
                applicationRecord.getAppliedAt(),
                applicationRecord.getCreatedAt(),
                applicationRecord.getUpdatedAt(),
                applicationRecord.getStatusUpdatedAt(),
                daysInCurrentStatus,
                needsAttention,
                attentionLevel,
                attentionMessage,
                applicationRecord.getNotes()
        );
    }

    public ApplicationRecord toApplicationRecord(ApplicationRecordRequestDto requestDto, User user) {
        return new ApplicationRecord(
                user,
                requestDto.getCompanyName(),
                requestDto.getPositionTitle(),
                requestDto.getJobPostingUrl(),
                requestDto.getPrimaryContactName(),
                requestDto.getPrimaryContactPhone(),
                requestDto.getPrimaryContactEmail(),
                requestDto.getStatus(),
                requestDto.getAppliedAt(),
                null,
                null,
                null,
                requestDto.getNotes()
        );
    }
}

