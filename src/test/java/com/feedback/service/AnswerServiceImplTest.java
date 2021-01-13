package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.feedback.service.FeedbackRequestServiceImpl.END_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AnswerServiceImplTest {
    @Mock
    private FeedbackRepo feedbackRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private QuestionRepo questionRepo;
    @Mock
    private FeedbackRequestRepo feedbackRequestRepo;

    @InjectMocks
    private AnswerServiceImpl answerService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(userRepo, questionRepo, feedbackRepo, feedbackRequestRepo);
    }

    @Test
    void testCreateAnswer() {
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.ofNullable(feedbackRequest()));
        when(feedbackRepo.findByFeedbackRequestId(1L)).thenReturn(listOfFeedback());
        when(questionRepo.findById(10L)).thenReturn(Optional.ofNullable(question()));
        when(userRepo.findTeacherById(147L)).thenReturn(Optional.ofNullable(user()));
        when(feedbackRepo.save(listOfFeedback().get(0))).thenReturn(listOfFeedback().get(0));
        Answer answer = answerService.createAnswer(1L, 1L, 10L, 147L);
        assertNotNull(answer);
        assertEquals(answer.getQuestionId(), question().getId());
        assertEquals(answer.getTeacherId(), user().getId());
        assertEquals(0, answer.getRate());
        assertNull(answer.getComment());
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackRepo).findByFeedbackRequestId(1L);
        verify(questionRepo).findById(10L);
        verify(userRepo).findTeacherById(147L);
        List<Feedback> feedbackList = listOfFeedback();
        feedbackList.forEach(feedback -> feedback.getAnswer().add(answer));
        feedbackList.forEach(feedback -> verify(feedbackRepo).save(feedback));
    }

    @Test
    void testGetAnswersByFeedbackRequestId() {
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.of(feedbackRequest()));
        when(feedbackRepo.findByFeedbackRequestId(1L)).thenReturn(listOfFeedback());
        when(questionRepo.findById(10L)).thenReturn(Optional.of(question()));
        when(userRepo.findTeacherById(147L)).thenReturn(Optional.of(user()));
        Set<AnswerDto> answerDtoResult = answerService.getAnswersByFeedbackRequestId(1L, 1L);
        assertNotNull(answerDtoResult);
        answerDtoResult.forEach(item -> {
            assertEquals(item.getTeacher().getEmail(), user().getEmail());
            assertEquals(item.getQuestion(), question().getQuestionValue());
        });
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackRepo).findByFeedbackRequestId(1L);
        verify(questionRepo).findById(10L);
        verify(userRepo).findTeacherById(147L);
    }

    private FeedbackRequest feedbackRequest() {
        return FeedbackRequest.builder()
                .id(1L)
                .course(Course.builder().id(1L).build())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(END_DATE))
                .build();
    }

    private Question question() {
        return Question.builder()
                .id(10L)
                .isPattern(true)
                .isRateable(true)
                .questionValue("First question")
                .build();
    }

    private User user() {
        return User.builder()
                .id(147L)
                .firstName("FirstUserName")
                .lastName("FirstLastName")
                .email("first@mail.com")
                .role(Role.TEACHER)
                .password("12345")
                .feedbackRequests(new HashSet<>())
                .build();
    }

    private List<Feedback> listOfFeedback() {
        return List.of(
                Feedback.builder()
                        .isClosed(false)
                        .userId(5L)
                        .feedbackRequestId(1L)
                        .answer(answerSet())
                        .build(),
                Feedback.builder()
                        .isClosed(false)
                        .userId(4L)
                        .feedbackRequestId(1L)
                        .answer(answerSet())
                        .build()
        );
    }

    private Set<Answer> answerSet() {
        Set<Answer> answerSet = new HashSet<>();
        answerSet.add(Answer.builder()
                .teacherId(147L)
                .rate(5)
                .questionId(10L)
                .build());
        return answerSet;
    }
}
