package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface CourseService {
    CourseDto create(CourseDto dto);

    CourseDto get(Long id);

    List<CourseDto> getAll();

    CourseDto update(CourseDto dto);

    CourseDto delete(CourseDto dto);

    Set<UserDto> getCourseTeachers(Long courseId);

    Set<UserDto> getCourseStudents(Long courseId);

    Set<UserDto> getStudentsNotFromCourse(Long courseId);

    Set<UserDto> getTeachersNotFromCourse(Long courseId);

    UserDto addTeacherToCourseById(Long courseId, UserDto teacher);

    UserDto addStudentToCourseById(Long courseId, UserDto student);

    ResponseEntity<String> deleteUserFromCourse(Long courseId, UserDto userDto);

    UserDto courseAddUser(Long courseId, Long userId);

}
