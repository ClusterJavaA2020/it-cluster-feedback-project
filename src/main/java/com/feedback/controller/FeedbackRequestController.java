package com.feedback.controller;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.service.FeedbackRequestService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin
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
