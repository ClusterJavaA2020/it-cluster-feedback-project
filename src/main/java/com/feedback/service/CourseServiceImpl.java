package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.exceptions.CourseNotFoundException;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.CourseDto.map;

@Slf4j
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
        log.info("Creating new course {}", dto);
        return map(courseRepo.save(map(dto)));
    }

    @Override
    public CourseDto get(Long id) {
        log.info("Receiving course id {}", id);
        return courseRepo.findById(id).map(CourseDto::map).orElseThrow(CourseNotFoundException::new);
    }

    @Override
    public List<CourseDto> getAll() {
        log.info("Receiving all courses");
        return courseRepo.findByOrderByStartDateDesc().stream()
                .map(CourseDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto update(CourseDto dto) {
        log.info("Updating course {}", dto);
        return map(courseRepo.save(map(dto)));
    }

    @Override
    public CourseDto delete(CourseDto dto) {
        log.info("Deleting course {}", dto);
        return null;
    }

    @Override
    public Set<UserDto> getCourseTeachers(Long courseId) {
        log.info("Receiving teachers from course {}", courseId);
        return courseRepo.findById(courseId).map(value -> value
                .getUsers()
                .stream()
                .filter(u -> u.getRole().equals(Role.TEACHER))
                .map(UserDto::map)
                .collect(Collectors.toSet())).orElse(null);
    }

    @Override
    public Set<UserDto> getCourseStudents(Long courseId) {
        log.info("Receiving students from course {}", courseId);
        return courseRepo.findById(courseId).map(value -> value
                .getUsers()
                .stream()
                .filter(u -> u.getRole().equals(Role.USER))
                .map(UserDto::map)
                .collect(Collectors.toSet())).orElse(null);
    }

    @Override
    public Set<UserDto> getStudentsNotFromCourse(Long courseId) {
        log.info("Receiving students not from course {}", courseId);
        return userRepo.findStudentsNotFromCourse(courseId).stream()
                .map(UserDto::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserDto> getTeachersNotFromCourse(Long courseId) {
        log.info("Receiving teachers not from course {}", courseId);
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
        log.info("Adding teacher {} to course {}", user, courseId);
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
        log.info("Adding student {} to course {}", user, courseId);
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
        log.info("Deleting user {} from course {}", userDto, courseId);
        return new ResponseEntity<>("WRONG PARAMETERS", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDto courseAddUser(Long userId, Long courseId) {
        User user = userRepo.findById(userId).orElse(null);
        courseRepo.saveUserInCourse(userId, courseId);
        log.info("Saving user {} in course {}", userId, courseId);
        return UserDto.map(user);
    }

}
