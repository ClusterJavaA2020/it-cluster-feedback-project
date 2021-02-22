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

    @Modifying
    @Query(value = "select * from feedback_requests where is_active = true and is_finished = false ",nativeQuery = true)
    @Transactional
    List<FeedbackRequest> findAllByActiveIsTrueAndFinishedIsFalse();

}
