package com.feedback.repo;

import com.feedback.repo.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepo extends MongoRepository<Feedback, String> {
    Optional<Feedback> findById(String feedbackId);

    List<Feedback> findByFeedbackRequestId(Long feedbackRequestId);

    List<Feedback> findByUserIdAndCourseId(Long userId, Long courseId);

    List<Feedback> findByCourseIdAndFeedbackRequestIdAndSubmittedTrue(Long courseId, Long feedbackRequestId);

    List<Feedback> findByActiveTrueAndSubmittedFalse();

    List<Feedback> findByActiveTrueAndSubmittedFalseAndCourseIdAndFeedbackRequestId(Long courseId, Long feedbackRequestId);

    List<Feedback> findAllByFeedbackRequestId(int id);

    List<Feedback> findByCourseId(Long courseId);
}
