package com.feedback.service;

import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final FeedbackRepo feedbackRepo;
    private final UserRepo userRepo;
    private final QuestionRepo questionRepo;

    public AnswerServiceImpl(FeedbackRepo feedbackRepo, UserRepo userRepo, QuestionRepo questionRepo) {
        this.feedbackRepo = feedbackRepo;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
    }

    @Override
    public Answer createAnswer(Long feedbackRequestId, Long questionId, Long aboutUserId) {
        List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
        Optional<Question> question = questionRepo.findById(questionId);
        Optional<User> teacher = userRepo.findById(aboutUserId);
        if (question.isPresent() && feedbackList.size() > 0 &&
                ((teacher.isPresent() && teacher.get().getRole().equals(Role.TEACHER)) || aboutUserId == 0)) {
            Answer answer = Answer.builder()
                    .questionId(questionId)
                    .aboutUserId(aboutUserId == 0 ? null : aboutUserId)
                    .rate(question.get().isRateable() ? 0 : null)
                    .comment(question.get().isRateable() ? null : "")
                    .build();
            feedbackList.forEach(f -> f.getAnswer().add(answer));
            feedbackList.forEach(feedbackRepo::save);
            return answer;
        } else {
            return null;
        }
    }

    @Override
    public Set<Answer> getQuestionsByFeedbackRequestId(Long feedbackRequestId) {
        List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
        if (feedbackList.size() > 0) {
            return feedbackList.get(0).getAnswer();
        } else {
            return null;
        }
    }
}
