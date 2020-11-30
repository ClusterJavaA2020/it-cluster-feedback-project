package com.feedback.controller;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.service.FeedbackRequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class FeedbackRequestController {
    private FeedbackRequestService feedbackRequestService;

    public FeedbackRequestController(FeedbackRequestService feedbackRequestService) {
        this.feedbackRequestService = feedbackRequestService;
    }

    @PostMapping("/feedback-request")
    public FeedbackRequestDto createFeedbackRequest(@RequestParam Long courseId) {
        return feedbackRequestService.createFeedbackRequest(courseId);
    }
}
