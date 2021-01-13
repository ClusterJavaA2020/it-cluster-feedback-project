package com.feedback.repo;

import com.feedback.dto.CourseDto;
import com.feedback.repo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Long> {

}
