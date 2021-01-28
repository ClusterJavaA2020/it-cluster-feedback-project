package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.entity.User;

import java.util.Set;

public interface CourseService {
    CourseDto create(CourseDto dto);

    CourseDto get(Long id);

    Set<CourseDto> getAll();

    CourseDto update(CourseDto dto);

    CourseDto delete(CourseDto dto);

    Set<UserDto> getCourseTeachers(Long courseId);

    Set<UserDto> getCourseStudents(Long courseId);

    User courseAddUser(Long courseId, Long userId);
}
