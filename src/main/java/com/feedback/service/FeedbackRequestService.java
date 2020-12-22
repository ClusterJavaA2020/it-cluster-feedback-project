package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;

import java.util.List;

public interface FeedbackRequestService {
    FeedbackRequestDto createFeedbackRequest(Long courseId);

    List<FeedbackRequestDto> getFeedbackRequestList(Long courseId);

    FeedbackRequestDto getFeedbackRequestById(Long feedbackRequestId);
}
