package com.feedback.controller;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.UserDto;
import com.feedback.model.Answer;
import com.feedback.service.AnswerService;
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

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(answerService);
    }

    @WithMockUser
    @Test
    void testCreateAnswer() throws Exception {
        when(answerService.createAnswer(2L, 105L, 3L, 4L)).thenReturn(answer());
        MvcResult mvcResult = mockMvc
                .perform(post("/courses/2/feedback-requests/105/answers")
                        .param("questionId", "3")
                        .param("teacherId", "4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(answerService).createAnswer(2L, 105L, 3L, 4L);
    }

    @WithMockUser
    @Test
    void testGetAnswersByFeedbackRequestId() throws Exception {
        when(answerService.getAnswersByFeedbackRequestId(2L, 105L)).thenReturn(Set.of(answerDto()));
        MvcResult mvcResult = mockMvc
                .perform(get("/courses/2/feedback-requests/105/answers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(answerService).getAnswersByFeedbackRequestId(2L, 105L);
    }

    @WithMockUser
    @Test
    void testDeleteAnswer() throws Exception {
        when(answerService.deleteAnswer(2L, 105L, 3L, 4L)).thenReturn(Set.of(answerDto()));
        MvcResult mvcResult = mockMvc
                .perform(delete("/courses/2/feedback-requests/105/answers?questionId=3&teacherId=4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(answerService).deleteAnswer(2L, 105L, 3L, 4L);
    }

    private Answer answer() {
        return Answer.builder()
                .comment("comment")
                .questionId(3L)
                .rate(5)
                .teacherId(4L)
                .build();
    }

    private AnswerDto answerDto() {
        return AnswerDto.builder()
                .comment("comment")
                .question("question")
                .rate(4)
                .teacher(UserDto.builder().build())
                .build();
    }
}
