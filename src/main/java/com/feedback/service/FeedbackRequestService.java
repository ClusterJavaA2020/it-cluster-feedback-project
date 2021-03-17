package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.dto.UserDto;
import com.feedback.util.SwitcherDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface FeedbackRequestService {
    FeedbackRequestDto createFeedbackRequest(Long courseId);

    List<FeedbackRequestDto> getFeedbackRequestList(Long courseId);

    FeedbackRequestDto getFeedbackRequestById(Long courseId, Long feedbackRequestId);

    FeedbackRequestDto activateFeedbackRequest(Long courseId, Long feedbackRequestId, SwitcherDto switcherDto);

    ResponseEntity<String> deleteFeedbackRequest(Long courseId, Long feedbackRequestId);

    FeedbackRequestDto finishFeedbackRequestSwitcher(Long courseId, Long feedbackRequestId, SwitcherDto switcherDto);

    void reminder();

    void finishedFeedbackRequests();

    Set<UserDto> remindUsersWithoutFeedback(Long courseId, Long feedbackRequestId);
}
