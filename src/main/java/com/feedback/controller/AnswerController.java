package com.feedback.controller;

import com.feedback.dto.AnswerDto;
import com.feedback.model.Answer;
import com.feedback.service.AnswerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
                               @RequestParam Long questionId,
                               @RequestParam Long teacherId) {
        return answerService.createAnswer(courseId, feedbackRequestId, questionId, teacherId);
    }

    @GetMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/answers")
    public Set<AnswerDto> getAnswersByFeedbackRequestId(@PathVariable Long courseId,
                                                        @PathVariable Long feedbackRequestId) {
        return answerService.getAnswersByFeedbackRequestId(courseId, feedbackRequestId);
    }

    @DeleteMapping("/courses/{courseId}/feedback-requests/{feedbackRequestId}/answers")
    public Set<AnswerDto> deleteAnswer(@PathVariable Long courseId,
                                       @PathVariable Long feedbackRequestId,
                                       @RequestParam Long questionId,
                                       @RequestParam Long teacherId) {
        return answerService.deleteAnswer(courseId, feedbackRequestId, questionId, teacherId);
    }
}

