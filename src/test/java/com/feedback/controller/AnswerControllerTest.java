package com.feedback.controller;

import com.feedback.model.Answer;
import com.feedback.service.AnswerService;
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

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AnswerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerService answerService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @WithMockUser
    @Test
    void testCreateAnswer() throws Exception {
        when(answerService.createAnswer(1L, 1L, 1L)).thenReturn(answer());
        MvcResult mvcResult = mockMvc
                .perform(post("/api/answers/")
                        .param("feedbackRequestId", "1")
                        .param("questionId", "1")
                        .param("aboutUserId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
    }

    @WithMockUser
    @Test
    void testGetQuestionsByFeedbackRequestId() throws Exception {
        when(answerService.getQuestionsByFeedbackRequestId(105L)).thenReturn(Set.of(answer()));
        MvcResult mvcResult = mockMvc
                .perform(get("/api/answers/")
                        .param("feedbackRequestId", "105")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
    }

    private Answer answer() {
        return Answer.builder()
                .build();
    }
}
