package com.feedback.controller;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.dto.UserDto;
import com.feedback.service.FeedbackRequestService;
import com.feedback.util.SwitcherDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/courses/")
public class FeedbackRequestController {
    private final FeedbackRequestService feedbackRequestService;

    public FeedbackRequestController(FeedbackRequestService feedbackRequestService) {
        this.feedbackRequestService = feedbackRequestService;
    }

    @PostMapping("{courseId}/feedback-requests")
    public FeedbackRequestDto createFeedbackRequest(@PathVariable Long courseId) {
        return feedbackRequestService.createFeedbackRequest(courseId);
    }

    @GetMapping("{courseId}/feedback-requests")
    public List<FeedbackRequestDto> getFeedbackRequestList(@PathVariable Long courseId) {
        return feedbackRequestService.getFeedbackRequestList(courseId);
    }

    @GetMapping("{courseId}/feedback-requests/{feedbackRequestId}")
    public FeedbackRequestDto getFeedbackRequestById(@PathVariable Long courseId, @PathVariable Long feedbackRequestId) {
        return feedbackRequestService.getFeedbackRequestById(courseId, feedbackRequestId);
    }

    @PutMapping("{courseId}/feedback-requests/{feedbackRequestId}/activation")
    public FeedbackRequestDto activateFeedbackRequest(@PathVariable Long courseId,
                                                      @PathVariable Long feedbackRequestId,
                                                      @RequestBody SwitcherDto switcherDto) {
        return feedbackRequestService.activateFeedbackRequest(courseId, feedbackRequestId, switcherDto);
    }

    @PutMapping("{courseId}/feedback-requests/{feedbackRequestId}/finish")
    public FeedbackRequestDto finishFeedbackRequest(@PathVariable Long courseId,
                                                    @PathVariable Long feedbackRequestId,
                                                    @RequestBody SwitcherDto switcherDto) {
        return feedbackRequestService.finishFeedbackRequestSwitcher(courseId, feedbackRequestId, switcherDto);
    }

    @DeleteMapping("{courseId}/feedback-requests/{feedbackRequestId}")
    public ResponseEntity<String> deleteFeedbackRequest(@PathVariable Long courseId,
                                                        @PathVariable Long feedbackRequestId) {
        return feedbackRequestService.deleteFeedbackRequest(courseId, feedbackRequestId);
    }

    @PostMapping("{courseId}/feedback-requests/{feedbackRequestId}/reminding")
    public Set<UserDto> remindUsersWithoutFeedback(@PathVariable Long courseId, @PathVariable Long feedbackRequestId) {
        return feedbackRequestService.remindUsersWithoutFeedback(courseId, feedbackRequestId);
    }
}
