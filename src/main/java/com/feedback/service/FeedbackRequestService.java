package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.util.SwitcherDto;

import java.util.List;

public interface FeedbackRequestService {
    FeedbackRequestDto createFeedbackRequest(Long courseId);

    List<FeedbackRequestDto> getFeedbackRequestList(Long courseId);

    FeedbackRequestDto getFeedbackRequestById(Long courseId, Long feedbackRequestId);

    FeedbackRequestDto updateFeedbackRequestActivation(Long courseId, Long feedbackRequestId, SwitcherDto switcherDto);
}
