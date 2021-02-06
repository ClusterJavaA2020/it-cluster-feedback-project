package com.feedback.repo;

import com.feedback.repo.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepo extends MongoRepository<Feedback, String> {
    Optional<Feedback> findById(String feedbackRequestId);
    List<Feedback> findByFeedbackRequestId(Long id);
    List<Feedback>  findByIsActiveTrueAndIsSubmittedFalse();
    List<Feedback> findAllByFeedbackRequestId(int id);
}
