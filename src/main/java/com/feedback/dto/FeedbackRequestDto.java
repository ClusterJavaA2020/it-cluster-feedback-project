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
    private boolean active;
    private boolean finished;
    private String courseTitle;
    private Long courseId;

    public static FeedbackRequestDto map(FeedbackRequest feedbackRequest) {
        return FeedbackRequestDto.builder()
                .id(feedbackRequest.getId())
                .startDate(feedbackRequest.getStartDate())
                .endDate(feedbackRequest.getEndDate())
                .active(feedbackRequest.isActive())
                .finished(feedbackRequest.isFinished())
                .courseTitle(feedbackRequest.getCourse().getTitle())
                .courseId(feedbackRequest.getCourse().getId())
                .build();
    }
}
