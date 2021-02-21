package com.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.dto.FeedbackRequestDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.User;
import com.feedback.service.FeedbackRequestService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.feedback.dto.FeedbackRequestDto.map;
import static com.feedback.service.FeedbackRequestServiceImpl.END_DATE;
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
class FeedbackRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackRequestService feedbackRequestService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(feedbackRequestService);
    }

    @WithMockUser
    @Test
    void testCreateFeedbackRequest() throws Exception {
        when(feedbackRequestService.createFeedbackRequest(15L)).thenReturn(feedbackRequestDto());
        MvcResult mvcResult = mockMvc
                .perform(post("/courses/15/feedback-requests")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackRequestService).createFeedbackRequest(15L);
    }

    @WithMockUser
    @Test
    void testGetFeedbackRequestList() throws Exception {
        when(feedbackRequestService.getFeedbackRequestList(15L)).thenReturn(List.of(feedbackRequestDto()));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/15/feedback-requests")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackRequestService).getFeedbackRequestList(15L);
    }

    @WithMockUser
    @Test
    void testGetFeedbackRequestById() throws Exception {
        when(feedbackRequestService.getFeedbackRequestById(15L, 1L)).thenReturn(feedbackRequestDto());
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/15/feedback-requests/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackRequestService).getFeedbackRequestById(15L, 1L);
    }

    @WithMockUser
    @Test
    void testActivateFeedbackRequest() throws Exception {
        when(feedbackRequestService.activateFeedbackRequest(15L, 1L, switcherDto()))
                .thenReturn(feedbackRequestDto());
        MvcResult mvcResult = mockMvc
                .perform(put("/courses/15/feedback-requests/1/activation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(switcherDto())))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackRequestService).activateFeedbackRequest(15L, 1L, switcherDto());
    }

    @WithMockUser
    @Test
    void testDeleteFeedbackRequest() throws Exception {
        when(feedbackRequestService.deleteFeedbackRequest(15L, 1L))
                .thenReturn(new ResponseEntity<>("REMOVED", HttpStatus.NO_CONTENT));
        MvcResult mvcResult = mockMvc
                .perform(delete("/courses/15/feedback-requests/1"))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(feedbackRequestService).deleteFeedbackRequest(15L, 1L);
    }

    @WithMockUser
    @Test
    void finishFeedbackRequest() throws Exception {
        when(feedbackRequestService.finishFeedbackRequestSwitcher(15L, 1L, switcherDto()))
                .thenReturn(feedbackRequestDto());
        MvcResult mvcResult = mockMvc
                .perform(put("/courses/15/feedback-requests/1/finish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(switcherDto())))
                .andExpect(status().isOk())
                .andReturn();
        verify(feedbackRequestService).finishFeedbackRequestSwitcher(15L, 1L, switcherDto());

    }

    @WithMockUser
    @Test
    void testRemindUsersWithoutFeedback() throws Exception {
        when(feedbackRequestService.remindUsersWithoutFeedback(15L, 1L))
                .thenReturn(Set.of(UserDto.map(user())));
        MvcResult mvcResult = mockMvc
                .perform(post("/courses/15/feedback-requests/1/reminding")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(feedbackRequestService).remindUsersWithoutFeedback(15L, 1L);
    }

    private Course course() {
        return Course.builder()
                .id(15L)
                .title("Mock course")
                .description("This is the test course.")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(4))
                .build();
    }

    private FeedbackRequest feedbackRequest() {
        return FeedbackRequest.builder()
                .id(1L)
                .course(course())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(END_DATE))
                .build();
    }

    private FeedbackRequestDto feedbackRequestDto() {
        return map(feedbackRequest());
    }

    private SwitcherDto switcherDto() {
        return SwitcherDto.builder()
                .active(true)
                .build();
    }

    private User user() {
        return User.builder()
                .id(32L)
                .email("controller@gmail.com")
                .firstName("FirstName")
                .lastName("LastName")
                .build();
    }
}
