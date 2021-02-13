package com.feedback.controller;

import com.feedback.dto.CourseDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.dto.UserDto;
import com.feedback.service.UserService;
import org.junit.jupiter.api.AfterEach;
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

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(userService);
    }

    @WithMockUser
    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(7L)).thenReturn(userDto());
        MvcResult mvcResult = mockMvc
                .perform(get("/users/7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(userService).getUserById(7L);
    }

    @WithMockUser
    @Test
    void testGetUserCoursesByUserId() throws Exception {
        when(userService.getUserCoursesByUserId(7L)).thenReturn(Set.of(CourseDto.builder().build()));
        MvcResult mvcResult = mockMvc
                .perform(get("/users/7/courses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(userService).getUserCoursesByUserId(7L);
    }

    @WithMockUser
    @Test
    void testGetFeedbackByUserIdAndCourseId() throws Exception {
        when(userService.getFeedbackByUserIdAndCourseId(7L, 3L)).thenReturn(List.of(FeedbackDto.builder().build()));
        MvcResult mvcResult = mockMvc
                .perform(get("/users/7/courses/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(userService).getFeedbackByUserIdAndCourseId(7L, 3L);
    }

    private UserDto userDto() {
        return UserDto.builder()
                .id(7L)
                .firstName("User")
                .lastName("Dto")
                .email("someemail@gmail.com")
                .build();
    }
}
