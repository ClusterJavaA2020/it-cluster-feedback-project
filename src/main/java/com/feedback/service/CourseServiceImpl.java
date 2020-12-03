package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.repo.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.CourseDto.map;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepo courseRepo;

    @Override
    public CourseDto create(CourseDto dto) {
        return map(courseRepo.save(map(dto)));
    }

    @Override
    public CourseDto get(Long id) {
        return map(courseRepo.getOne(id));
    }

    @Override
    public Set<CourseDto> getAll() {
        return courseRepo.findAll().stream()
                .map(CourseDto:: map)
                .collect(Collectors.toSet());
    }

    @Override
    public CourseDto update(CourseDto dto) {
        return map(courseRepo.save(map(dto)));
    }

    @Override
    public CourseDto delete(CourseDto dto) {
        return null;
    }
}
