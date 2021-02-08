package com.feedback.controller;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Set<CourseDto> getCourses() {
        return courseService.getAll();
    }

    @PostMapping
    public CourseDto create(@RequestBody CourseDto dto) {
        return courseService.create(dto);
    }

    @PutMapping
    public CourseDto update(@RequestBody CourseDto dto) {
        return courseService.update(dto);
    }

    @GetMapping("{courseId}")
    public CourseDto getCourseById(@PathVariable Long courseId) {
        return courseService.get(courseId);
    }

    @GetMapping("{courseId}/teachers")
    public Set<UserDto> getCourseTeachers(@PathVariable Long courseId) {
        return courseService.getCourseTeachers(courseId);
    }

    @GetMapping("{courseId}/students")
    public Set<UserDto> getCourseStudents(@PathVariable Long courseId) {
        return courseService.getCourseStudents(courseId);
    }

    @PostMapping("/addUser/{userId}/{courseId}")
    @PreAuthorize("hasAuthority('admin:create')")
    public UserDto courseAddUser(@PathVariable Long userId, @PathVariable Long courseId) {
        return courseService.courseAddUser(userId,courseId);
    }

    @GetMapping("/{courseId}/not-students")
    public Set<UserDto> getStudentsNotFromCourse(@PathVariable Long courseId) {
        return courseService.getStudentsNotFromCourse(courseId);
    }

    @GetMapping("/{courseId}/not-teachers")
    public Set<UserDto> getTeachersNotFromCourse(@PathVariable Long courseId) {
        return courseService.getTeachersNotFromCourse(courseId);
    }
}
