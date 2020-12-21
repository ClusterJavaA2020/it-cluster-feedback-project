package com.feedback.service;

import com.feedback.repo.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions();

    List<Question> getPatterns();

    List<Question> getNonPatterns();
}
