package com.feedback.service;

import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.feedback.dto.QuestionDto.map;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(questions(), questionList);
        verify(questionRepo).findAll();
    }

    @Test
    void testGetPatterns() {
        when(questionRepo.findByIsPatternTrue()).thenReturn(List.of(questions().get(1)));
        List<Question> questionList = questionService.getPatterns();
        assertEquals(List.of(questions().get(1)), questionList);
        verify(questionRepo).findByIsPatternTrue();
    }

    @Test
    void testGetNonPatterns() {
        when(questionRepo.findByIsPatternFalse()).thenReturn(List.of(questions().get(0)));
        List<Question> questionList = questionService.getNonPatterns();
        assertEquals(List.of(questions().get(0)), questionList);
        verify(questionRepo).findByIsPatternFalse();
    }

    @Test
    void testGetQuestionById() {
        when(questionRepo.findById(2L)).thenReturn(ofNullable(questions().get(1)));
        Question question = questionService.getQuestionById(2L);
        assertEquals(question, questions().get(1));
        verify(questionRepo).findById(2L);
    }

    @Test
    void addQuestion() {
        when(questionRepo.findByQuestionValue("Some first custom question?")).thenReturn(questions().get(0));
        when(questionRepo.save(questions().get(0))).thenReturn(questions().get(0));
        Question question = questionService.addQuestion(map(questions().get(0)));
        assertEquals(questions().get(0), question);
        verify(questionRepo).findByQuestionValue("Some first custom question?");
        verify(questionRepo).save(questions().get(0));
    }

    private List<Question> questions() {
        return List.of(Question.builder()
                        .id(1L)
                        .isPattern(false)
                        .questionValue("Some first custom question?")
                        .build(),
                Question.builder()
                        .id(2L)
                        .isPattern(true)
                        .questionValue("Some second custom question?")
                        .build()
        );
    }

}
