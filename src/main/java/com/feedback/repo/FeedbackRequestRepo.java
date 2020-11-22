package com.feedback.repo;

import com.feedback.repo.entity.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRequestRepo extends JpaRepository<FeedbackRequest, Long> {
}
