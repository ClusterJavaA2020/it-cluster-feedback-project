package com.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedbackCounterDto {
    private int allFeedback;
    private int activeFeedback;
    private int newFeedback;
    private int notSubmittedFeedback;
}
