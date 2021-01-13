package com.feedback.repo;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.repo.entity.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRequestRepo extends JpaRepository<FeedbackRequest, Long> {
    List<FeedbackRequestDto> findByCourseId(Long courseId);

    @Query(value = "SELECT user_id from user_feedback_request",nativeQuery = true)
    List<Long> userId();
}
