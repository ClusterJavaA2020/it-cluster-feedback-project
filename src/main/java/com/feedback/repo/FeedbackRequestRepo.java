package com.feedback.repo;

import com.feedback.repo.entity.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface FeedbackRequestRepo extends JpaRepository<FeedbackRequest, Long> {
    List<FeedbackRequest> findByCourseIdOrderByIdDesc(Long courseId);

    Set<FeedbackRequest> findByIdIn(Set<Long> idSet);

    List<FeedbackRequest> findByActiveTrueAndFinishedFalse();

}
