package com.feedback.service;

import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class QuestionServiceImplTest {
    @Mock
    private QuestionRepo questionRepo;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(questionRepo);
    }

    @Test
    void testGetAllQuestions() {
        when(questionRepo.findAll()).thenReturn(questions());
        List<Question> questionList = questionService.getAllQuestions();
        Assertions.assertEquals(questions(), questionList);
        verify(questionRepo).findAll();
    }

    @Test
    void testGetPatterns() {
        when(questionRepo.findByIsPatternTrue()).thenReturn(List.of(questions().get(1)));
        List<Question> questionList = questionService.getPatterns();
        Assertions.assertEquals(List.of(questions().get(1)), questionList);
        verify(questionRepo).findByIsPatternTrue();
    }

    @Test
    void testGetNonPatterns() {
        when(questionRepo.findByIsPatternFalse()).thenReturn(List.of(questions().get(0)));
        List<Question> questionList = questionService.getNonPatterns();
        Assertions.assertEquals(List.of(questions().get(0)), questionList);
        verify(questionRepo).findByIsPatternFalse();
    }

    private List<Question> questions() {
        return List.of(Question.builder()
                        .id(1L)
                        .isRateable(true)
                        .isPattern(false)
                        .questionValue("Some first question?")
                        .build(),
                Question.builder()
                        .id(2L)
                        .isRateable(false)
                        .isPattern(true)
                        .questionValue("Some second question?")
                        .build()
        );
    }
}
