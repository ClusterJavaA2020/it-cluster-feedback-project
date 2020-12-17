package com.feedback.controller;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.service.FeedbackRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static com.feedback.dto.FeedbackRequestDto.map;
import static com.feedback.service.FeedbackRequestServiceImpl.END_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void createFeedbackRequest() throws Exception {
        when(feedbackRequestService.createFeedbackRequest(1L)).thenReturn(feedbackRequestDto());
        MvcResult mvcResult = mockMvc
                .perform(post("/feedback-request")
                        .param("courseId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        assertEquals(feedbackRequestDto(), map(feedbackRequest()));
    }

    private Course course() {
        return Course.builder()
                .id(1L)
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
}