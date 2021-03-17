package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.UserDto;
import com.feedback.exceptions.CourseNotFoundException;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.CourseDto.map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CourseServiceImplTest {
    @Mock
    private CourseRepo courseRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(courseRepo, userRepo);
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
    void testGetNegativeCase() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.get(anyLong()));
        verify(courseRepo).findById(anyLong());
    }

    @Test
    void testGetAll() {
        when(courseRepo.findByOrderByStartDateDesc()).thenReturn(List.of(course()));
        List<CourseDto> courseDtoSet = courseService.getAll();
        assertNotNull(courseDtoSet);
        assertEquals(List.of(map(course())), courseDtoSet);
        verify(courseRepo).findByOrderByStartDateDesc();
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

    @Test
    void testGetStudentsNotFromCourse() {
        when(userRepo.findStudentsNotFromCourse(37L))
                .thenReturn(userList().stream().filter(user -> user.getRole().equals(Role.USER)).collect(Collectors.toSet()));
        Set<UserDto> userDtoSet = courseService.getStudentsNotFromCourse(37L);
        assertFalse(userDtoSet.isEmpty());
        assertEquals(userList().stream()
                .filter(user -> user.getRole().equals(Role.USER))
                .map(UserDto::map).collect(Collectors.toSet()), userDtoSet);
        verify(userRepo).findStudentsNotFromCourse(37L);
    }

    @Test
    void testGetTeachersNotFromCourse() {
        when(userRepo.findTeachersNotFromCourse(39L)).thenReturn(Set.of(teacher()));
        Set<UserDto> userDtoSet = courseService.getTeachersNotFromCourse(39L);
        assertFalse(userDtoSet.isEmpty());
        assertEquals(userList().stream()
                .filter(user -> user.getRole().equals(Role.TEACHER))
                .map(UserDto::map).collect(Collectors.toSet()), userDtoSet);
        verify(userRepo).findTeachersNotFromCourse(39L);
    }

    @Test
    void testAddTeacherToCourseById() {
        when(courseRepo.findById(54L)).thenReturn(Optional.ofNullable(course()));
        when(userRepo.findTeacherById(5L)).thenReturn(Optional.ofNullable(teacher()));
        when(userRepo.save(teacher())).thenReturn(teacher());
        UserDto userDto = courseService.addTeacherToCourseById(54L, UserDto.builder().id(5L).build());
        assertEquals(UserDto.map(teacher()), userDto);
        verify(courseRepo).findById(54L);
        verify(userRepo).findTeacherById(5L);
        verify(userRepo).save(teacher());
    }

    @Test
    void testAddTeacherToCourseByIdNegativeCase() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepo.findTeacherById(anyLong())).thenReturn(Optional.empty());
        UserDto userDto = courseService.addTeacherToCourseById(54L, UserDto.builder().id(5L).build());
        assertNull(userDto);
        verify(courseRepo, times(1)).findById(anyLong());
        verify(userRepo, times(1)).findTeacherById(anyLong());
        verify(userRepo, times(0)).save(teacher());
    }

    @Test
    void testAddStudentToCourseById() {
        when(courseRepo.findById(54L)).thenReturn(Optional.ofNullable(course()));
        when(userRepo.findStudentById(6L)).thenReturn(Optional.ofNullable(student()));
        when(userRepo.save(student())).thenReturn(student());
        UserDto userDto = courseService.addStudentToCourseById(54L, UserDto.builder().id(6L).build());
        assertEquals(UserDto.map(student()), userDto);
        verify(courseRepo).findById(54L);
        verify(userRepo).findStudentById(6L);
        verify(userRepo).save(student());
    }

    @Test
    void testAddStudentToCourseByIdNegativeCase() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepo.findStudentById(anyLong())).thenReturn(Optional.empty());
        UserDto userDto = courseService.addStudentToCourseById(54L, UserDto.builder().id(6L).build());
        assertNull(userDto);
        verify(courseRepo, times(1)).findById(anyLong());
        verify(userRepo, times(1)).findStudentById(anyLong());
        verify(userRepo, times(0)).save(student());
    }

    @Test
    void testDeleteUserFromCourse() {
        when(userRepo.findById(12L)).thenReturn(Optional.ofNullable(student()));
        ResponseEntity<String> result = courseService.deleteUserFromCourse(10L, UserDto.builder().id(12L).build());
        assertEquals(new ResponseEntity<>("REMOVED", HttpStatus.NO_CONTENT), result);
        verify(userRepo).findById(12L);
        verify(userRepo).save(student());
    }

    @Test
    void testDeleteUserFromCourseNegativeCase() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<String> result = courseService.deleteUserFromCourse(10L, UserDto.builder().id(12L).build());
        assertEquals(new ResponseEntity<>("WRONG PARAMETERS", HttpStatus.BAD_REQUEST), result);
        verify(userRepo, times(1)).findById(anyLong());
        verify(userRepo, times(0)).save(student());
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
        return Set.of(teacher(), student());
    }

    private User teacher() {
        return User.builder()
                .firstName("Teacher's first name")
                .lastName("Teacher's last name")
                .email("teacher@gmail.com")
                .role(Role.TEACHER)
                .courses(new HashSet<>())
                .active(true)
                .build();
    }

    private User student() {
        return User.builder()
                .firstName("User's first name")
                .lastName("User's last name")
                .email("user@gmail.com")
                .role(Role.USER)
                .courses(new HashSet<>())
                .active(true)
                .build();
    }
}
