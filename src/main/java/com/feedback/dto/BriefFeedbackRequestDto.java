package com.feedback.dto;

import com.feedback.repo.entity.FeedbackRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class BriefFeedbackRequestDto {
    private Long id;
    private LocalDate endDate;

    public static BriefFeedbackRequestDto map(FeedbackRequest feedbackRequest) {
        return BriefFeedbackRequestDto.builder()
                .id(feedbackRequest.getId())
                .endDate(feedbackRequest.getEndDate())
                .build();
    }
}
