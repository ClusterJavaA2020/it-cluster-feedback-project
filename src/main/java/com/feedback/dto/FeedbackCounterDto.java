package com.feedback.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedbackCounterDto {
    private Integer allFeedback;
    private Long activeFeedback;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long newFeedback;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long notSubmittedFeedback;
}
