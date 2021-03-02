package com.feedback.controller;

import com.feedback.dto.QuestionDto;
import com.feedback.repo.entity.Question;
import com.feedback.service.QuestionService;
import com.feedback.util.SwitcherDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
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

    @GetMapping("/{questionId}")
    public Question getQuestionById(@PathVariable Long questionId) {
        return questionService.getQuestionById(questionId);
    }

    @PostMapping
    public Question addQuestion(@RequestBody QuestionDto questionDto) {
        return questionService.addQuestion(questionDto);
    }

    @PostMapping("/{id}/pattern")
    public boolean isPattern(@RequestBody SwitcherDto switcherDto, @PathVariable Long id) {
        return questionService.togglePattern(switcherDto.isActive(), id);
    }
}
