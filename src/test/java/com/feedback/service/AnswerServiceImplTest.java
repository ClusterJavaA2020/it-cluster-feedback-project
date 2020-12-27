package com.feedback.service;

import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AnswerServiceImplTest {
    @Mock
    private FeedbackRepo feedbackRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private QuestionRepo questionRepo;

    @InjectMocks
    private AnswerServiceImpl answerService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testCreateAnswer() {
        when(feedbackRepo.findByFeedbackRequestId(1L)).thenReturn(listOfFeedback());
        when(questionRepo.findById(10L)).thenReturn(Optional.ofNullable(question()));
        when(userRepo.findById(147L)).thenReturn(Optional.ofNullable(user()));
        Answer answer = answerService.createAnswer(1L, 10L, 147L);
        assertNotNull(answer);
        assertEquals(answer.getQuestionId(), question().getId());
        assertEquals(answer.getAboutUserId(), user().getId());
        assertEquals(answer.getRate(), 0);
        assertNull(answer.getComment());

        List<Feedback> feedbackList = listOfFeedback();
        feedbackList.forEach(feedback -> feedback.getAnswer().add(answer));
        feedbackList.forEach(feedback -> verify(feedbackRepo).save(feedback));
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
                        .answer(new HashSet<>())
                        .build(),
                Feedback.builder()
                        .isClosed(false)
                        .userId(4L)
                        .feedbackRequestId(1L)
                        .answer(new HashSet<>())
                        .build()
        );
    }
}
