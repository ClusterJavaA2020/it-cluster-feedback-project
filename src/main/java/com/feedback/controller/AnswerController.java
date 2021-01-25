package com.feedback.controller;

import com.feedback.dto.AnswerDto;
import com.feedback.model.Answer;
import com.feedback.service.AnswerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/answers")
    public Answer createAnswer(@PathVariable Long courseId,
                               @PathVariable Long feedbackRequestId,
                               @RequestBody Answer answer) {
        return answerService.createAnswer(courseId, feedbackRequestId, answer);
    }

    @GetMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/answers")
    public Set<AnswerDto> getAnswersByFeedbackRequestId(@PathVariable Long courseId,
                                                        @PathVariable Long feedbackRequestId) {
        return answerService.getAnswersByFeedbackRequestId(courseId, feedbackRequestId);
    }

    @DeleteMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/answers")
    public Set<Answer> deleteAnswer(@PathVariable Long courseId,
                                    @PathVariable Long feedbackRequestId,
                                    @RequestBody Answer answer) {
        return answerService.deleteAnswer(courseId, feedbackRequestId, answer);
    }
}

