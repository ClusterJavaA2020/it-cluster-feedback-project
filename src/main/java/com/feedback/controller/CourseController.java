package com.feedback.controller;

import com.feedback.dto.CourseDto;
import com.feedback.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    Set<CourseDto> getCourses() {
        return courseService.getAll();
    }

    @PostMapping
    CourseDto create(@RequestBody CourseDto dto) {
        return courseService.create(dto);
    }

    @PutMapping
    CourseDto update(@RequestBody CourseDto dto) {
        return courseService.update(dto);
    }

}
