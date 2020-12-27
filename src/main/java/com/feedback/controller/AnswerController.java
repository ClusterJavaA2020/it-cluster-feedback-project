package com.feedback.controller;

import com.feedback.model.Answer;
import com.feedback.service.AnswerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public Answer createAnswer(@RequestParam Long feedbackRequestId,
                               @RequestParam Long questionId,
                               @RequestParam Long aboutUserId) {
        return answerService.createAnswer(feedbackRequestId, questionId, aboutUserId);
    }

    @GetMapping
    public Set<Answer> getQuestionsByFeedbackRequestId(@RequestParam Long feedbackRequestId) {
        return answerService.getQuestionsByFeedbackRequestId(feedbackRequestId);
    }
}

