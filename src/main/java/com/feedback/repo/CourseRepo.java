package com.feedback.repo;

import com.feedback.repo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, Long> {
}
