package com.feedback.service;

import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepo questionRepo;
    private final FeedbackRepo feedbackRepo;

    public QuestionServiceImpl(QuestionRepo questionRepo, FeedbackRepo feedbackRepo) {
        this.questionRepo = questionRepo;
        this.feedbackRepo = feedbackRepo;
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
}
