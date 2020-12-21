package com.feedback.controller;

import com.feedback.repo.entity.Question;
import com.feedback.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testGetAllQuestions() throws Exception {
        when(questionService.getAllQuestions()).thenReturn(listOfQuestions());
        MvcResult mvcResult = mockMvc
                .perform(get("/questions/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
    }

    @Test
    void testGetPatterns() throws Exception {
        when(questionService.getPatterns()).thenReturn(listOfQuestions());
        MvcResult mvcResult = mockMvc
                .perform(get("/questions/patterns")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

    }

    @Test
    void testGetNonPatterns() throws Exception {
        when(questionService.getNonPatterns()).thenReturn(listOfQuestions());
        MvcResult mvcResult = mockMvc
                .perform(get("/questions/non-patterns")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

    }

    private List<Question> listOfQuestions() {
        Question question1 = Question.builder()
                .id(1L)
                .questionValue("Test question 1")
                .isPattern(true)
                .isRateable(true)
                .build();
        Question question2 = Question.builder()
                .id(1L)
                .questionValue("Test question 2")
                .isPattern(false)
                .isRateable(false)
                .build();

        return List.of(question1, question2);
    }
}
