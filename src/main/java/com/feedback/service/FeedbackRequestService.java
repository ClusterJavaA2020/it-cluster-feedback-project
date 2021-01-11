package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.repo.entity.FeedbackRequest;

import java.util.List;

public interface FeedbackRequestService {
    FeedbackRequestDto createFeedbackRequest(Long courseId);

    List<FeedbackRequestDto> getFeedbackRequestList(Long courseId);

    FeedbackRequestDto getFeedbackRequestById(Long courseId, Long feedbackRequestId);

    FeedbackRequestDto updateFeedbackRequest(Long courseId, FeedbackRequest FeedbackRequest);
}
