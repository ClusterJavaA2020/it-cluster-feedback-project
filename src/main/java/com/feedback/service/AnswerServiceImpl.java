package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.UserDto;
import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final FeedbackRepo feedbackRepo;
    private final UserRepo userRepo;
    private final QuestionRepo questionRepo;
    private final FeedbackRequestRepo feedbackRequestRepo;

    public AnswerServiceImpl(FeedbackRepo feedbackRepo, UserRepo userRepo, QuestionRepo questionRepo,
                             FeedbackRequestRepo feedbackRequestRepo) {
        this.feedbackRepo = feedbackRepo;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.feedbackRequestRepo = feedbackRequestRepo;
    }

    @Override
    public Answer createAnswer(Long courseId, Long feedbackRequestId, Long questionId, Long teacherId) {
        List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
        Question question = questionRepo.findById(questionId).orElse(null);
        if (teacherId != 0 && userRepo.findTeacherById(teacherId).isEmpty()) {
            return null;
        }
        if (isValidRequestParams(courseId, feedbackRequestId) && question != null) {
            Answer answer = Answer.builder()
                    .questionId(questionId)
                    .teacherId(teacherId == 0 ? null : teacherId)
                    .rate(question.isRateable() ? 0 : null)
                    .comment(question.isRateable() ? null : "")
                    .build();
            feedbackList.forEach(f -> f.getAnswer().add(answer));
            feedbackList.forEach(feedbackRepo::save);
            return answer;
        }
        return null;
    }

    @Override
    public Set<AnswerDto> getAnswersByFeedbackRequestId(Long courseId, Long feedbackRequestId) {
        if (isValidRequestParams(courseId, feedbackRequestId)) {
            List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
            if (!feedbackList.isEmpty()) {
                Set<Answer> answers = feedbackList.stream().findFirst().map(Feedback::getAnswer).orElse(new HashSet<>());
                return mapAnswerSet(answers);
            }
        }
        return new HashSet<>();
    }

    @Override
    public Set<AnswerDto> deleteAnswer(Long courseId, Long feedbackRequestId, Long questionId, Long teacherId) {
        List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
        if (teacherId != 0 && userRepo.findTeacherById(teacherId).isEmpty()) {
            return new HashSet<>();
        }
        Long finalTeacherId = teacherId == 0L ? null : teacherId;
        if (isValidRequestParams(courseId, feedbackRequestId) && !feedbackList.isEmpty()) {
            feedbackList.stream()
                    .map(Feedback::getAnswer)
                    .forEach(answers ->
                            answers.removeIf(answer ->
                                    answer.getQuestionId().equals(questionId) &&
                                            Objects.equals(answer.getTeacherId(), finalTeacherId)
                            )
                    );
            feedbackRepo.saveAll(feedbackList);
            Set<Answer> answers = feedbackList.stream().findFirst().map(Feedback::getAnswer).orElse(new HashSet<>());
            return mapAnswerSet(answers);
        }
        return new HashSet<>();
    }

    private Set<AnswerDto> mapAnswerSet(Set<Answer> answerSet) {
        return answerSet.stream().map(answer ->
                AnswerDto.builder()
                        .questionId(answer.getQuestionId())
                        .question(questionRepo.findById(answer.getQuestionId()).map(Question::getQuestionValue).orElse(null))
                        .teacher(userRepo.findTeacherById(answer.getTeacherId()).map(UserDto::map).orElse(null))
                        .rate(answer.getRate())
                        .comment(answer.getComment())
                        .build()).collect(Collectors.toSet());
    }

    private boolean isValidRequestParams(Long courseId, Long feedbackRequestId) {
        FeedbackRequest feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId).orElse(null);
        return feedbackRequest != null && feedbackRequest.getCourse().getId().equals(courseId);
    }
}
