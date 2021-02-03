package com.feedback.service;

import com.feedback.dto.QuestionDto;
import com.feedback.repo.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions();

    List<Question> getPatterns();

    List<Question> getNonPatterns();

    Question getQuestionById(Long questionId);

    Question addQuestion(QuestionDto questionDto);

    int isPattern(boolean switcherDto,Long id);
}
