package com.feedback.repo;

import com.feedback.repo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
    List<Course> findByOrderByStartDateDesc();
}
