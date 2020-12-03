package com.feedback.service;

import com.feedback.dto.CourseDto;

import java.util.Set;

public interface CourseService {
    CourseDto create (CourseDto dto);

    CourseDto get(Long id);

    Set<CourseDto> getAll();

    CourseDto update(CourseDto dto);

    CourseDto delete(CourseDto dto);
}
