package com.feedback.service;

import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;
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

    @Mock
    private FeedbackRepo feedbackRepo;

    @Mock
    private AnswerService answerService;

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

    @Test
    void addCustomQuestion() {
        Question questionNew = questions().get(0);
        Question questionFromDb = questions().get(0);
        questionFromDb.setRateable(false);
        questionFromDb.setPattern(true);
        when(questionRepo.findByQuestionValue("Some first custom question?")).thenReturn(questionFromDb);
        when(questionRepo.save(questionNew)).thenReturn(questionFromDb);
        when(feedbackRepo.findByFeedbackRequestId(23L)).thenReturn(List.of(feedback()));
        when(feedbackRepo.saveAll(List.of(feedback()))).thenReturn(List.of(feedback()));
        when(answerService.createAnswer(1L, 23L, 1L, 2L)).thenReturn(answer());
        Question questionResult = questionService.addCustomQuestion(map(questionNew), 1L, 23L, 2L);
        assertEquals(questionNew, questionResult);
        verify(questionRepo).findByQuestionValue("Some first custom question?");
        verify(questionRepo).save(questions().get(0));
        verify(feedbackRepo).findByFeedbackRequestId(23L);
        verify(feedbackRepo).saveAll(List.of(feedback()));
        verify(answerService).createAnswer(1L, 23L, 1L, 2L);
    }

    private List<Question> questions() {
        return List.of(Question.builder()
                        .id(1L)
                        .isRateable(true)
                        .isPattern(false)
                        .questionValue("Some first custom question?")
                        .build(),
                Question.builder()
                        .id(2L)
                        .isRateable(false)
                        .isPattern(true)
                        .questionValue("Some second custom question?")
                        .build()
        );
    }

    private Feedback feedback() {
        return Feedback.builder()
                .isClosed(false)
                .feedbackRequestId(23L)
                .userId(1L)
                .isClosed(false)
                .answer(new HashSet<>())
                .build();
    }

    private Answer answer() {
        return Answer.builder()
                .teacherId(147L)
                .rate(0)
                .questionId(147L)
                .build();
    }
}
