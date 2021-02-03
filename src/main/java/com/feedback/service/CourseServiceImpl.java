package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.exceptions.CourseNotFoundException;
import com.feedback.exceptions.CourseCreateException;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.CourseDto.map;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;

    public CourseServiceImpl(CourseRepo courseRepo, UserRepo userRepo) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
    }

    @Override
    public CourseDto create(CourseDto dto) {
        try {
            return map(courseRepo.save(map(dto)));
        } catch (DataIntegrityViolationException e) {
            throw new CourseCreateException();
        }
    }

    @Override
    public CourseDto get(Long id) {
        return courseRepo.findById(id).map(CourseDto::map).orElseThrow(CourseNotFoundException::new);
    }

    @Override
    public Set<CourseDto> getAll() {
        return courseRepo.findAll().stream()
                .map(CourseDto::map)
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

    @Override
    public Set<UserDto> getCourseTeachers(Long courseId) {
        return courseRepo.findById(courseId).map(value -> value
                .getUsers()
                .stream()
                .filter(u -> u.getRole().equals(Role.TEACHER))
                .map(UserDto::map)
                .collect(Collectors.toSet())).orElse(null);
    }

    @Override
    public Set<UserDto> getCourseStudents(Long courseId) {
        return courseRepo.findById(courseId).map(value -> value
                .getUsers()
                .stream()
                .filter(u -> u.getRole().equals(Role.USER))
                .map(UserDto::map)
                .collect(Collectors.toSet())).orElse(null);
    }

    @Override
    public User courseAddUser(Long userId, Long courseId) {
        User user = userRepo.findUserById(userId);
        courseRepo.SaveInUserCourse(userId, courseId);
        return user;
    }
}
