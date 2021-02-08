package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.CourseDto.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CourseServiceImplTest {
    @Mock
    private CourseRepo courseRepo;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(courseRepo);
    }

    @Test
    void testCreate() {
        CourseDto dto = map(course());
        when(courseRepo.save(map(dto))).thenReturn(course());
        CourseDto courseDto = courseService.create(map(course()));
        assertNotNull(courseDto);
        assertEquals(dto, courseDto);
        verify(courseRepo).save(map(dto));
    }

    @Test
    void testGet() {
        when(courseRepo.findById(1L)).thenReturn(Optional.ofNullable(course()));
        CourseDto result = courseService.get(1L);
        assertNotNull(result);
        assertEquals(map(course()), result);
        verify(courseRepo).findById(1L);
    }

    @Test
    void testGetAll() {
        when(courseRepo.findAll()).thenReturn(List.of(course()));
        Set<CourseDto> courseDtoSet = courseService.getAll();
        assertNotNull(courseDtoSet);
        assertEquals(Set.of(map(course())), courseDtoSet);
        verify(courseRepo).findAll();
    }

    @Test
    void testUpdate() {
        CourseDto dto = map(course());
        when(courseRepo.save(map(dto))).thenReturn(course());
        CourseDto courseDto = courseService.update(map(course()));
        assertNotNull(courseDto);
        assertEquals(dto, courseDto);
        verify(courseRepo).save(map(dto));
    }

    @Test
    void testGetCourseTeachers() {
        when(courseRepo.findById(20L)).thenReturn(Optional.ofNullable(course()));
        Set<UserDto> teacherList = courseService.getCourseTeachers(20L);
        assertFalse(teacherList.isEmpty());
        assertEquals(userList().stream()
                        .filter(f -> f.getRole().equals(Role.TEACHER))
                        .map(UserDto::map)
                        .collect(Collectors.toSet()),
                teacherList);
        verify(courseRepo).findById(20L);
    }

    @Test
    void testGetCourseStudents() {
        when(courseRepo.findById(20L)).thenReturn(Optional.ofNullable(course()));
        Set<UserDto> studentsList = courseService.getCourseStudents(20L);
        assertFalse(studentsList.isEmpty());
        assertEquals(userList().stream()
                        .filter(f -> f.getRole().equals(Role.USER))
                        .map(UserDto::map)
                        .collect(Collectors.toSet()),
                studentsList);
        verify(courseRepo).findById(20L);
    }

    private Course course() {
        return Course.builder()
                .description("This is test course.")
                .title("Test course")
                .users(userList())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .build();
    }

    private Set<User> userList() {
        return Set.of(
                User.builder()
                        .firstName("Teacher's first name")
                        .lastName("Teacher's last name")
                        .email("teacher@gmail.com")
                        .role(Role.TEACHER)
                        .active(true)
                        .build(),
                User.builder()
                        .firstName("User's first name")
                        .lastName("User's last name")
                        .email("user@gmail.com")
                        .role(Role.USER)
                        .active(true)
                        .build()
        );
    }
}
