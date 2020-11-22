package com.feedback.repo;

import com.feedback.repo.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepo extends MongoRepository<Feedback, String> {
}
