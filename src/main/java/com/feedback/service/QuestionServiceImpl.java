package com.feedback.service;

import com.feedback.dto.QuestionDto;
import com.feedback.exceptions.QuestionNotFoundException;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.feedback.dto.QuestionDto.map;

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

    @Override
    public Question addQuestion(QuestionDto questionDto) {
        if (questionDto.getQuestionValue() == null) {
            return null;
        }
        Question question = questionRepo.findByQuestionValue(questionDto.getQuestionValue());
        if (question == null) {
            return questionRepo.save(map(questionDto));
        } else {
            question.setPattern(questionDto.isPattern());
            return questionRepo.save(question);
        }
    }

    @Override
    public Question togglePattern(boolean isPattern, Long id) {
        questionRepo.togglePattern(isPattern, id);
        return questionRepo.findById(id).orElseThrow(QuestionNotFoundException::new);
    }
}
