package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.FeedbackCounterDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.FeedbackDto.map;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepo feedbackRepo;
    private final FeedbackRequestRepo feedbackRequestRepo;
    private final QuestionRepo questionRepo;
    private final UserRepo userRepo;

    public FeedbackServiceImpl(FeedbackRepo feedbackRepo, FeedbackRequestRepo feedbackRequestRepo, QuestionRepo questionRepo, UserRepo userRepo) {
        this.feedbackRepo = feedbackRepo;
        this.feedbackRequestRepo = feedbackRequestRepo;
        this.questionRepo = questionRepo;
        this.userRepo = userRepo;
    }

    @Override
    public FeedbackDto getFeedbackById(Long courseId, Long feedbackRequestId, String feedbackId) {
        Feedback feedback = feedbackRepo.findById(feedbackId).orElse(null);
        if (feedback != null && feedback.getCourseId().equals(courseId) &&
                feedback.getFeedbackRequestId().equals(feedbackRequestId)) {
            Set<Long> userIdSet = new HashSet<>();
            userIdSet.add(feedback.getUserId());
            Set<Long> feedbackRequestIdSet = new HashSet<>();
            feedbackRequestIdSet.add(feedback.getFeedbackRequestId());
            Set<Long> questionIdSet = new HashSet<>();
            feedback.getAnswers().forEach(answer -> {
                questionIdSet.add(answer.getQuestionId());
                userIdSet.add(answer.getTeacherId());
            });
            Set<User> userSet = userRepo.findByIdIn(userIdSet);
            Set<FeedbackRequest> feedbackRequestSet = feedbackRequestRepo.findByIdIn(feedbackRequestIdSet);
            Set<Question> questionSet = questionRepo.findByIdIn(questionIdSet);
            return map(List.of(feedback), userSet, feedbackRequestSet, questionSet).stream().findFirst().orElse(null);
        }
        log.info("Receiving feedback {} for course {} by feedback request id {}", feedbackId, courseId, feedbackRequestId);
        return null;
    }

    @Override
    public List<FeedbackDto> getSubmittedFeedbackByFeedbackRequestId(Long courseId, Long feedbackRequestId) {
        List<Feedback> feedbackList = feedbackRepo.findByCourseIdAndFeedbackRequestIdAndSubmittedTrue(courseId, feedbackRequestId);
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> feedbackRequestIdSet = new HashSet<>();
        Set<Long> questionIdSet = new HashSet<>();
        feedbackList.forEach(feedback -> {
            userIdSet.add(feedback.getUserId());
            feedbackRequestIdSet.add(feedback.getFeedbackRequestId());
            feedback.getAnswers().forEach(answer -> {
                questionIdSet.add(answer.getQuestionId());
                userIdSet.add(answer.getTeacherId());
            });
        });
        Set<User> userSet = userRepo.findByIdIn(userIdSet);
        Set<FeedbackRequest> feedbackRequestSet = feedbackRequestRepo.findByIdIn(feedbackRequestIdSet);
        Set<Question> questionSet = questionRepo.findByIdIn(questionIdSet);
        log.info("Receiving submitted feedback by feedback request id {} for course {}", feedbackRequestId, courseId);
        return map(feedbackList, userSet, feedbackRequestSet, questionSet)
                .stream().sorted(Comparator.comparing(FeedbackDto::getDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Answer> updateFeedbackAnswers(Long courseId, Long feedbackRequestId,
                                              String feedbackId, List<AnswerDto> answerDtoList) {
        Feedback feedback = feedbackRepo.findById(feedbackId).orElse(null);
        if (feedback != null && feedback.getCourseId().equals(courseId) && feedback.getFeedbackRequestId().equals(feedbackRequestId)) {
            feedback.setSubmitted(true);
            feedback.setDate(LocalDateTime.now());
            feedback.setAnswers(answerDtoList.stream().map(AnswerDto::map).collect(Collectors.toCollection(LinkedHashSet::new)));
            return new ArrayList<>(feedbackRepo.save(feedback).getAnswers());
        }
        log.info("Updating answers {} in feedback {} for feedback request {} and course {}", answerDtoList, feedbackId, feedbackRequestId, courseId);
        return Collections.emptyList();
    }

    @Override
    public List<Feedback> getAllByFeedbackRequestId(int id) {
        log.info("Receiving all feedbacks by feedback request id {}", id);
        return feedbackRepo.findAllByFeedbackRequestId(id);
    }

    @Override
    public FeedbackCounterDto getCounterForUser(Long userId, Long courseId) {
        List<Feedback> feedbackList = feedbackRepo.findByUserIdAndCourseId(userId, courseId);
        return FeedbackCounterDto.builder()
                .allFeedback(feedbackList.size())
                .activeFeedback(feedbackList.stream().filter(f -> f.isActive() && f.isSubmitted()).count())
                .newFeedback(feedbackList.stream().filter(f -> f.isActive() && !f.isSubmitted()).count())
                .build();
    }

    @Override
    public FeedbackCounterDto getCounterForAdmin(Long courseId) {
        List<Feedback> feedbackList = feedbackRepo.findByCourseId(courseId);
        return FeedbackCounterDto.builder()
                .allFeedback(feedbackList.size())
                .activeFeedback(feedbackList.stream().filter(Feedback::isActive).count())
                .notSubmittedFeedback(feedbackList.stream().filter(f -> !f.isSubmitted() && f.isActive()).count())
                .build();
    }

}
