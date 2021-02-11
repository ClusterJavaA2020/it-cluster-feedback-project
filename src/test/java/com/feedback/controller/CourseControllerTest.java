package com.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.dto.UserDto;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.User;
import com.feedback.service.CourseService;
import com.feedback.util.SwitcherDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;

import static com.feedback.dto.CourseDto.map;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(courseService);
    }


    @WithMockUser
    @Test
    void testGetCourses() throws Exception {
        when(courseService.getAll()).thenReturn(List.of(map(course())));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).getAll();
    }

    @WithMockUser
    @Test
    void testCreate() throws Exception {
        when(courseService.create(map(course()))).thenReturn(map(course()));
        MvcResult mvcResult = mockMvc
                .perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(course())))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).create(map(course()));
    }

    @WithMockUser
    @Test
    void testUpdate() throws Exception {
        when(courseService.update(map(course()))).thenReturn(map(course()));
        MvcResult mvcResult = mockMvc
                .perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(course())))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).update(map(course()));
    }

    @WithMockUser
    @Test
    void testGetCourseById() throws Exception {
        when(courseService.get(1L)).thenReturn(map(course()));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).get(1L);
    }

    @WithMockUser
    @Test
    void testGetCourseTeachers() throws Exception {
        when(courseService.getCourseTeachers(1L))
                .thenReturn(Set.of(com.feedback.dto.UserDto.map((User.builder().build()))));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/1/teachers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).getCourseTeachers(1L);
    }

    @WithMockUser
    @Test
    void testGetCourseStudents() throws Exception {
        when(courseService.getCourseStudents(1L))
                .thenReturn(Set.of(com.feedback.dto.UserDto.map((User.builder().build()))));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).getCourseStudents(1L);
    }

    @WithMockUser
    @Test
    void testGetStudentsNotFromCourse() throws Exception {
        when(courseService.getStudentsNotFromCourse(1L))
                .thenReturn(Set.of(com.feedback.dto.UserDto.map((User.builder().build()))));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/1/not-students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).getStudentsNotFromCourse(1L);
    }

    @WithMockUser
    @Test
    void testGetTeachersNotFromCourse() throws Exception {
        when(courseService.getTeachersNotFromCourse(1L))
                .thenReturn(Set.of(com.feedback.dto.UserDto.map((User.builder().build()))));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/1/not-teachers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).getTeachersNotFromCourse(1L);

    }

    @WithMockUser
    @Test
    void testAddTeacherToCourseById() throws Exception {
        when(courseService.addTeacherToCourseById(1L, userDto()))
                .thenReturn(userDto());
        MvcResult mvcResult = mockMvc
                .perform(put("/courses/1/add-teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto())))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).addTeacherToCourseById(1L, userDto());
    }

    @WithMockUser
    @Test
    void testAddStudentToCourse() throws Exception {
        when(courseService.addStudentToCourseById(1L, userDto()))
                .thenReturn(userDto());
        MvcResult mvcResult = mockMvc
                .perform(put("/courses/1/add-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto())))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(courseService).addStudentToCourseById(1L, userDto());
    }

    @WithMockUser
    @Test
    void testDeleteUserFromCourse() throws Exception {
        when(courseService.deleteUserFromCourse(1L, userDto()))
                .thenReturn(new ResponseEntity<>("REMOVED", HttpStatus.NO_CONTENT));
        MvcResult mvcResult = mockMvc
                .perform(delete("/courses/1/delete-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto())))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(courseService).deleteUserFromCourse(1L, userDto());
    }

    private Course course() {
        return Course.builder().build();
    }

    private SwitcherDto switcherDto() {
        return SwitcherDto.builder()
                .active(true)
                .build();
    }

    private UserDto userDto() {
        return UserDto.builder()
                .id(3L)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@gmail.com")
                .build();
    }

}
