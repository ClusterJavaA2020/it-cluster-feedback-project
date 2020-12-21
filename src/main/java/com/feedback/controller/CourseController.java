package com.feedback.controller;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/list")
    public Set<CourseDto> getCourses() {
        return courseService.getAll();
    }

    @PostMapping("/create")
    public CourseDto create(@RequestBody CourseDto dto) {
        return courseService.create(dto);
    }

    @PutMapping("/update")
    public CourseDto update(@RequestBody CourseDto dto) {
        return courseService.update(dto);
    }

    @GetMapping("")
    public CourseDto getCourseById(@RequestParam Long courseId) {
        return courseService.get(courseId);
    }

    @GetMapping("/teachers")
    public Set<UserDto> getCourseTeachers(@RequestParam Long courseId) {
        return courseService.getCourseTeachers(courseId);
    }

    @GetMapping("/students")
    public Set<UserDto> getCourseStudents(@RequestParam Long courseId) {
        return courseService.getCourseStudents(courseId);
    }
}
