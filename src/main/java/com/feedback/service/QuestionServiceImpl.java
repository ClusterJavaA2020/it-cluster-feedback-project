package com.feedback.service;

import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepo questionRepo;

    public QuestionServiceImpl(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    @Override
    public List<Question> getPatterns() {
        return questionRepo.findByIsPatternTrue();
    }

    @Override
    public List<Question> getNonPatterns() {
        return questionRepo.findByIsPatternFalse();
    }

    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepo.findById(questionId).orElse(null);
    }
}
