package com.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.dto.AnswerDto;
import com.feedback.dto.BriefUserDto;
import com.feedback.dto.FeedbackCounterDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.model.Answer;
import com.feedback.service.FeedbackService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(feedbackService);
    }

    @WithMockUser
    @Test
    void testGetFeedbackById() throws Exception {
        when(feedbackService.getFeedbackById(14L, 3L, "601f237b7523466d6ee03e43"))
                .thenReturn(feedbackDto());
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/14/feedback-requests/3/feedback/601f237b7523466d6ee03e43")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackService).getFeedbackById(14L, 3L, "601f237b7523466d6ee03e43");
    }

    @WithMockUser
    @Test
    void testGetSubmittedFeedbackByFeedbackRequestId() throws Exception {
        when(feedbackService.getSubmittedFeedbackByFeedbackRequestId(14L, 3L))
                .thenReturn(List.of(feedbackDto()));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/14/feedback-requests/3/feedback")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackService).getSubmittedFeedbackByFeedbackRequestId(14L, 3L);
    }

    @WithMockUser
    @Test
    void testUpdateFeedbackAnswers() throws Exception {
        when(feedbackService.updateFeedbackAnswers(14L, 3L, "601f237b7523466d6ee03e43", answerDtoList()))
                .thenReturn(List.of(Answer.builder().build()));
        MvcResult mvcResult = mockMvc
                .perform(put("/courses/14/feedback-requests/3/feedback/601f237b7523466d6ee03e43")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(answerDtoList())))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackService).updateFeedbackAnswers(14L, 3L, "601f237b7523466d6ee03e43", answerDtoList());

    }

    @WithMockUser
    @Test
    void testGetFeedbackCounterForUser() throws Exception {
        when(feedbackService.getCounterForUser(1L, 14L)).thenReturn(feedbackCounterDto());
        MvcResult mvcResult = mockMvc
                .perform(get("/users/1/courses/14/counter")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackService).getCounterForUser(1L, 14L);
    }

    @WithMockUser
    @Test
    void testGetFeedbackCounterForAdmin() throws Exception {
        when(feedbackService.getCounterForAdmin(14L)).thenReturn(feedbackCounterDto());
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/14/counter")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(feedbackService).getCounterForAdmin(14L);
    }

    private FeedbackDto feedbackDto() {
        return FeedbackDto.builder()
                .id("601f237b7523466d6ee03e43")
                .courseId(14L)
                .build();
    }

    private List<AnswerDto> answerDtoList() {
        return List.of(AnswerDto.builder()
                        .questionId(1L)
                        .question("Some question")
                        .teacher(BriefUserDto.builder()
                                .id(45L)
                                .firstName("Teacher")
                                .build())
                        .comment("comment")
                        .build(),
                AnswerDto.builder()
                        .questionId(2L)
                        .question("Some question")
                        .rate(10)
                        .build());
    }

    private FeedbackCounterDto feedbackCounterDto() {
        return FeedbackCounterDto.builder()
                .newFeedback(1L)
                .activeFeedback(5L)
                .allFeedback(2)
                .notSubmittedFeedback(4L)
                .build();
    }
}
