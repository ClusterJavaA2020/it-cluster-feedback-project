package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.BriefUserDto;
import com.feedback.model.Answer;
import com.feedback.repo.FeedbackAnswersRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.FeedbackAnswers;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnswerServiceImpl implements AnswerService {
    private final UserRepo userRepo;
    private final QuestionRepo questionRepo;
    private final FeedbackRequestRepo feedbackRequestRepo;
    private final FeedbackAnswersRepo feedbackAnswersRepo;

    public AnswerServiceImpl(UserRepo userRepo, QuestionRepo questionRepo, FeedbackRequestRepo feedbackRequestRepo,
                             FeedbackAnswersRepo feedbackAnswersRepo) {
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.feedbackRequestRepo = feedbackRequestRepo;
        this.feedbackAnswersRepo = feedbackAnswersRepo;
    }

    @Override
    public Answer createAnswer(Long courseId, Long feedbackRequestId, Answer answer) {
        FeedbackRequest feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId).orElse(null);
        FeedbackAnswers feedbackAnswers = feedbackAnswersRepo.findByFeedbackRequestId(feedbackRequestId);
        Question question = questionRepo.findById(answer.getQuestionId()).orElse(null);
        if (answer.getTeacherId() != null && userRepo.findTeacherById(answer.getTeacherId()).isEmpty()) {
            return null;
        }
        if (isValidRequestParams(courseId, feedbackRequest) && !feedbackRequest.isActive() && question != null) {
            feedbackAnswers.getAnswers().add(answer);
            feedbackAnswersRepo.save(feedbackAnswers);
            return answer;
        }
        log.info("Creating answer {} for course {} and feedback request {}", answer, courseId, feedbackRequestId);
        return null;
    }

    @Override
    public Set<AnswerDto> getAnswersByFeedbackRequestId(Long courseId, Long feedbackRequestId) {
        FeedbackRequest feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId).orElse(null);
        if (isValidRequestParams(courseId, feedbackRequest)) {
            FeedbackAnswers feedbackAnswers = feedbackAnswersRepo.findByFeedbackRequestId(feedbackRequestId);
            if (feedbackAnswers != null) {
                return feedbackAnswers.getAnswers().stream().map(answer ->
                        AnswerDto.builder()
                                .questionId(answer.getQuestionId())
                                .question(questionRepo.findById(answer.getQuestionId()).map(Question::getQuestionValue).orElse(null))
                                .teacher(userRepo.findTeacherById(answer.getTeacherId()).map(BriefUserDto::map).orElse(null))
                                .rate(answer.getRate())
                                .comment(answer.getComment())
                                .build()).collect(Collectors.toCollection(LinkedHashSet::new));
            }
        }
        log.info("Receiving answers for course {} BY feedback request id {}", courseId, feedbackRequestId);
        return Collections.emptySet();
    }

    @Override
    public Set<Answer> deleteAnswer(Long courseId, Long feedbackRequestId, Answer answerToRemove) {
        FeedbackAnswers feedbackAnswers = feedbackAnswersRepo.findByFeedbackRequestId(feedbackRequestId);
        FeedbackRequest feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId).orElse(null);
        if (isValidRequestParams(courseId, feedbackRequest) && !feedbackRequest.isActive()) {
            feedbackAnswers.getAnswers().removeIf(a -> a.equals(answerToRemove));
            return feedbackAnswersRepo.save(feedbackAnswers).getAnswers();
        }
        log.info("Deleting answer {} for course {} and feedback request {}", answerToRemove, courseId, feedbackRequestId);
        return Collections.emptySet();
    }

    private boolean isValidRequestParams(Long courseId, FeedbackRequest feedbackRequest) {
        log.info("Checking if the feedbackRequest {} equals courseId {}", feedbackRequest, courseId);
        return feedbackRequest != null && feedbackRequest.getCourse().getId().equals(courseId);
    }
}
