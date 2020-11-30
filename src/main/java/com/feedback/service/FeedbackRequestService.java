package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;

public interface FeedbackRequestService {
    FeedbackRequestDto createFeedbackRequest(Long courseId);
}
