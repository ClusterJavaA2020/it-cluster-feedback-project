package com.feedback.service;

import com.feedback.dto.QuestionDto;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.feedback.dto.QuestionDto.map;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepo questionRepo;
    private final FeedbackRepo feedbackRepo;
    private final AnswerService answerService;

    public QuestionServiceImpl(QuestionRepo questionRepo, FeedbackRepo feedbackRepo, AnswerService answerService) {
        this.questionRepo = questionRepo;
        this.feedbackRepo = feedbackRepo;
        this.answerService = answerService;
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
            question.setRateable(questionDto.isRateable());
            return questionRepo.save(question);
        }
    }

    @Override
    public Question addCustomQuestion(QuestionDto questionDto, Long courseId, Long feedbackRequestId, Long teacherId) {
        if (questionDto.getQuestionValue() == null) {
            return null;
        }
        Question question = questionRepo.findByQuestionValue(questionDto.getQuestionValue());
        if (question == null) {
            question = questionRepo.save(map(questionDto));
        } else {
            if (questionDto.isRateable() != question.isRateable()) {
                List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
                Question finalQuestion = question;
                feedbackList.stream()
                        .map(Feedback::getAnswer)
                        .forEach(answers ->
                                answers.stream()
                                        .filter(answer -> Objects.equals(answer.getQuestionId(), finalQuestion.getId()))
                                        .forEach(answer -> {
                                                    if (answer.getRate() == null) {
                                                        answer.setRate(0);
                                                        answer.setComment(null);
                                                    } else {
                                                        answer.setRate(null);
                                                        answer.setComment("");
                                                    }
                                                }
                                        )
                        );
                feedbackRepo.saveAll(feedbackList);
                question.setRateable(questionDto.isRateable());
            }
            question.setPattern(questionDto.isPattern());
            questionRepo.save(question);
        }
        answerService.createAnswer(courseId, feedbackRequestId, question.getId(), teacherId);
        return question;
    }
}
