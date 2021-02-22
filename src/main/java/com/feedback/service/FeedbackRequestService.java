package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.util.SwitcherDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FeedbackRequestService {
    FeedbackRequestDto createFeedbackRequest(Long courseId);

    List<FeedbackRequestDto> getFeedbackRequestList(Long courseId);

    FeedbackRequestDto getFeedbackRequestById(Long courseId, Long feedbackRequestId);

    FeedbackRequestDto activateFeedbackRequest(Long courseId, Long feedbackRequestId, SwitcherDto switcherDto);

    ResponseEntity<String> deleteFeedbackRequest(Long courseId, Long feedbackRequestId);

    FeedbackRequestDto finishFeedbackRequestSwitcher(Long courseId, Long feedbackRequestId, SwitcherDto switcherDto);

    void reminder();

    void finishedFeedbackRequests();
}
