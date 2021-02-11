package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return map(courseRepo.save(map(dto)));
    }

    @Override
    public CourseDto get(Long id) {
        return map(courseRepo.getOne(id));
    }

    @Override
    public List<CourseDto> getAll() {
        return courseRepo.findByOrderByStartDateDesc().stream().map(CourseDto::map).collect(Collectors.toList());
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
    public Set<UserDto> getStudentsNotFromCourse(Long courseId) {
        return userRepo.findStudentsNotFromCourse(courseId).stream()
                .map(UserDto::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserDto> getTeachersNotFromCourse(Long courseId) {
        return userRepo.findTeachersNotFromCourse(courseId).stream()
                .map(UserDto::map)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto addTeacherToCourseById(Long courseId, UserDto user) {
        Course course = courseRepo.findById(courseId).orElse(null);
        User teacher = userRepo.findTeacherById(user.getId()).orElse(null);
        if (course != null && teacher != null) {
            teacher.getCourses().add(course);
            return UserDto.map(userRepo.save(teacher));
        }
        return null;
    }

    @Override
    public UserDto addStudentToCourseById(Long courseId, UserDto user) {
        Course course = courseRepo.findById(courseId).orElse(null);
        User student = userRepo.findStudentById(user.getId()).orElse(null);
        if (course != null && student != null) {
            student.getCourses().add(course);
            return UserDto.map(userRepo.save(student));
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteUserFromCourse(Long courseId, UserDto userDto) {
        User user = userRepo.findById(userDto.getId()).orElse(null);
        if (user != null) {
            user.getCourses().removeIf(c -> c.getId().equals(courseId));
            userRepo.save(user);
            return new ResponseEntity<>("REMOVED", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("WRONG PARAMETERS", HttpStatus.BAD_REQUEST);
    }

}
