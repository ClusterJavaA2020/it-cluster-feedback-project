package com.feedback.service;

import com.feedback.dto.QuestionDto;
import com.feedback.exceptions.QuestionNotFoundException;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.feedback.dto.QuestionDto.map;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepo questionRepo;

    public QuestionServiceImpl(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public List<Question> getAllQuestions() {
        log.info("Receiving all questions");
        return questionRepo.findAll();
    }

    @Override
    public List<Question> getPatterns() {
        log.info("Receiving pattern questions");
        return questionRepo.findByPatternTrue();
    }

    @Override
    public List<Question> getNonPatterns() {
        log.info("Receiving non pattern questions");
        return questionRepo.findByPatternFalse();
    }

    @Override
    public Question getQuestionById(Long questionId) {
        log.info("Receiving question by id {}", questionId);
        return questionRepo.findById(questionId).orElse(null);
    }

    @Override
    public Question addQuestion(QuestionDto questionDto) {
        if (questionDto.getQuestionValue() == null) {
            return null;
        }
        Question question = questionRepo.findByQuestionValue(questionDto.getQuestionValue());
        log.info("Adding new question {}", questionDto);
        if (question == null) {
            return questionRepo.save(map(questionDto));
        } else {
            question.setPattern(questionDto.isPattern());
            return questionRepo.save(question);
        }
    }

    @Override
    public boolean togglePattern(boolean isPattern, Long id) {
        questionRepo.togglePattern(isPattern, id);
        Question question = questionRepo.findById(id).orElseThrow(QuestionNotFoundException::new);
        log.info("Toggling pattern {} for question by question id {}", isPattern, id);
        return question.isPattern();
    }
}
