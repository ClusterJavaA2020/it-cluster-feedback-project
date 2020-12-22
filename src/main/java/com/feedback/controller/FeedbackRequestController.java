package com.feedback.controller;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.service.FeedbackRequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feedback-requests")
public class FeedbackRequestController {
    private final FeedbackRequestService feedbackRequestService;

    public FeedbackRequestController(FeedbackRequestService feedbackRequestService) {
        this.feedbackRequestService = feedbackRequestService;
    }

    @PostMapping("/create")
    public FeedbackRequestDto createFeedbackRequest(@RequestParam Long courseId) {
        return feedbackRequestService.createFeedbackRequest(courseId);
    }

    @GetMapping("/all")
    public List<FeedbackRequestDto> getFeedbackRequestList(@RequestParam Long courseId) {
        return feedbackRequestService.getFeedbackRequestList(courseId);
    }

    @GetMapping
    public FeedbackRequestDto getFeedbackRequestById(@RequestParam Long feedbackRequestId){
        return feedbackRequestService.getFeedbackRequestById(feedbackRequestId);
    }
}
