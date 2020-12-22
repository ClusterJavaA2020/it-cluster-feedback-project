package com.feedback.dto;

import com.feedback.repo.entity.FeedbackRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class FeedbackRequestDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private String courseTitle;

    public static FeedbackRequestDto map(FeedbackRequest feedbackRequest) {
        return FeedbackRequestDto.builder()
                .id(feedbackRequest.getId())
                .startDate(feedbackRequest.getStartDate())
                .endDate(feedbackRequest.getEndDate())
                .isActive(feedbackRequest.isActive())
                .courseTitle(feedbackRequest.getCourse().getTitle())
                .build();
    }
}
