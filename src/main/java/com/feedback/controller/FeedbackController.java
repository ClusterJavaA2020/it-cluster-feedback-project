package com.feedback.controller;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.FeedbackCounterDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.model.Answer;
import com.feedback.repo.entity.Feedback;
import com.feedback.service.FeedbackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/feedback/{feedbackId}")
    public FeedbackDto getFeedbackById(@PathVariable Long courseId,
                                       @PathVariable Long feedbackRequestId,
                                       @PathVariable String feedbackId) {
        return feedbackService.getFeedbackById(courseId, feedbackRequestId, feedbackId);
    }

    @GetMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/feedback")
    public List<FeedbackDto> getSubmittedFeedbackByFeedbackRequestId(@PathVariable Long courseId,
                                                                     @PathVariable Long feedbackRequestId) {
        return feedbackService.getSubmittedFeedbackByFeedbackRequestId(courseId, feedbackRequestId);
    }

    @PutMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/feedback/{feedbackId}")
    public List<Answer> updateFeedbackAnswers(@PathVariable Long courseId,
                                              @PathVariable Long feedbackRequestId,
                                              @PathVariable String feedbackId,
                                              @RequestBody List<AnswerDto> answerDtoList) {
        return feedbackService.updateFeedbackAnswers(courseId, feedbackRequestId, feedbackId, answerDtoList);
    }

    @GetMapping("/feedback/{id}")
    public List<Feedback> getAllByFeedbackRequestId(@PathVariable int id) {
        return feedbackService.getAllByFeedbackRequestId(id);
    }

    @GetMapping("/users/{userId}/courses/{courseId}/counter")
    public FeedbackCounterDto getCounterForUser(@PathVariable Long userId, @PathVariable Long courseId) {
        return feedbackService.getCounterForUser(userId, courseId);
    }

    @GetMapping("/courses/{courseId}/counter")
    public FeedbackCounterDto getCounterForAdmin(@PathVariable Long courseId) {
        return feedbackService.getCounterForAdmin(courseId);
    }
}
