package com.feedback.repo;

import com.feedback.repo.entity.FeedbackAnswers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackAnswersRepo extends MongoRepository<FeedbackAnswers, String> {
    FeedbackAnswers findByFeedbackRequestId(Long feedbackRequestId);
}
