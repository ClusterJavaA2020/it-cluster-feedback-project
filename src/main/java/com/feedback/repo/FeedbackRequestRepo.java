package com.feedback.repo;

import com.feedback.repo.entity.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface FeedbackRequestRepo extends JpaRepository<FeedbackRequest, Long> {
    List<FeedbackRequest> findByCourseIdOrderByIdDesc(Long courseId);

    Set<FeedbackRequest> findByIdIn(Set<Long> idSet);

    List<FeedbackRequest> findByActiveTrueAndFinishedFalse();

}
