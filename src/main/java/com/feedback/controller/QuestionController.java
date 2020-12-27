package com.feedback.controller;

import com.feedback.repo.entity.Question;
import com.feedback.service.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/patterns")
    public List<Question> getPatterns() {
        return questionService.getPatterns();
    }

    @GetMapping("/non-patterns")
    public List<Question> getNonPatterns() {
        return questionService.getNonPatterns();
    }
}
