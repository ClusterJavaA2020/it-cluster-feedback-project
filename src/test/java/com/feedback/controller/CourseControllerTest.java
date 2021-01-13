package com.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.User;
import com.feedback.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static com.feedback.dto.CourseDto.map;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
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

    @WithMockUser
    @Test
    void testGetCourses() throws Exception {
        when(courseService.getAll()).thenReturn(Set.of(map(course())));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
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
    }

    @WithMockUser
    @Test
    void testGetCourseStudents() throws Exception {
        when(courseService.getCourseTeachers(1L))
                .thenReturn(Set.of(com.feedback.dto.UserDto.map((User.builder().build()))));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
    }

    private Course course() {
        return Course.builder().build();
    }
}
