package com.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.repo.entity.Question;
import com.feedback.service.QuestionService;
import com.feedback.util.SwitcherDto;
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

import static com.feedback.dto.QuestionDto.map;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(questionService);
    }

    @WithMockUser
    @Test
    void testGetAllQuestions() throws Exception {
        when(questionService.getAllQuestions()).thenReturn(listOfQuestions());
        MvcResult mvcResult = mockMvc
                .perform(get("/questions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(questionService).getAllQuestions();
    }

    @WithMockUser
    @Test
    void testGetPatterns() throws Exception {
        when(questionService.getPatterns()).thenReturn(listOfQuestions());
        MvcResult mvcResult = mockMvc
                .perform(get("/questions/patterns")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(questionService).getPatterns();

    }

    @WithMockUser
    @Test
    void testGetNonPatterns() throws Exception {
        when(questionService.getNonPatterns()).thenReturn(listOfQuestions());
        MvcResult mvcResult = mockMvc
                .perform(get("/questions/non-patterns")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(questionService).getNonPatterns();
    }

    @WithMockUser
    @Test
    void testGetQuestionById() throws Exception {
        when(questionService.getQuestionById(1L)).thenReturn(listOfQuestions().get(0));
        MvcResult mvcResult = mockMvc
                .perform(get("/questions/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(questionService).getQuestionById(1L);
    }

    @WithMockUser
    @Test
    void testAddQuestion() throws Exception {
        when(questionService.addQuestion(map(listOfQuestions().get(0)))).thenReturn(listOfQuestions().get(0));
        MvcResult mvcResult = mockMvc
                .perform(post("/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(map(listOfQuestions().get(0)))
                        )
                )
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(questionService).addQuestion(map(listOfQuestions().get(0)));
    }

    @WithMockUser
    @Test
    void isPattern() throws Exception {
        when(questionService.togglePattern(true, 1L)).thenReturn(true);
        MvcResult mvcResult = mockMvc
                .perform(post("/questions/1/pattern")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(SwitcherDto.builder().active(true).build())
                        )
                )
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(questionService).togglePattern(true, 1L);

    }

    private List<Question> listOfQuestions() {
        Question question1 = Question.builder()
                .id(1L)
                .questionValue("Test question 1")
                .pattern(true)
                .build();
        Question question2 = Question.builder()
                .id(2L)
                .questionValue("Test question 2")
                .pattern(false)
                .build();

        return List.of(question1, question2);
    }
}
