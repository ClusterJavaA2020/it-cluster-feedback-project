package com.feedback.repo;

import com.feedback.repo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
    List<Course> findByOrderByStartDateDesc();

    @Modifying
    @Query(value = "INSERT INTO user_course (user_id,course_id) VALUE (:userId,:courseId)", nativeQuery = true)
    @Transactional
    void saveUserInCourse(Long userId, Long courseId);
}
