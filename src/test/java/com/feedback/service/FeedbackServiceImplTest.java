package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.BriefUserDto;
import com.feedback.dto.FeedbackCounterDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.feedback.dto.FeedbackDto.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class FeedbackServiceImplTest {
    @Mock
    private FeedbackRepo feedbackRepo;
    @Mock
    private FeedbackRequestRepo feedbackRequestRepo;
    @Mock
    private QuestionRepo questionRepo;
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(feedbackRepo, feedbackRequestRepo, questionRepo, userRepo);
    }

    @Test
    void testGetFeedbackById() {
        when(feedbackRepo.findById("6025991aa9c08c230ed6f39a")).thenReturn(java.util.Optional.ofNullable(feedback()));
        when(userRepo.findByIdIn(setOf(2L, 7L))).thenReturn(setOf(student(), teacher()));
        when(feedbackRequestRepo.findByIdIn(setOf(4L))).thenReturn(setOf(feedbackRequest()));
        when(questionRepo.findByIdIn(setOf(10L))).thenReturn(setOf(question()));
        FeedbackDto result = feedbackService.getFeedbackById(3L, 4L, "6025991aa9c08c230ed6f39a");
        assertEquals(feedbackDto(), result);
        verify(feedbackRepo).findById("6025991aa9c08c230ed6f39a");
        verify(userRepo).findByIdIn(setOf(2L, 7L));
        verify(feedbackRequestRepo).findByIdIn(setOf(4L));
        verify(questionRepo).findByIdIn(setOf(10L));
    }

    @Test
    void testGetFeedbackByIdNegative() {
        when(feedbackRepo.findById("6025991aa9c08c230ed6f39a")).thenReturn(Optional.empty());
        FeedbackDto result = feedbackService.getFeedbackById(3L, 4L, "6025991aa9c08c230ed6f39a");
        assertNull(result);
        verify(feedbackRepo, times(1)).findById(anyString());
        verify(userRepo, times(0)).findByIdIn(anySet());
        verify(feedbackRequestRepo, times(0)).findByIdIn(anySet());
        verify(questionRepo, times(0)).findByIdIn(anySet());
    }

    @Test
    void testGetSubmittedFeedbackByFeedbackRequestId() {
        when(feedbackRepo.findByCourseIdAndFeedbackRequestIdAndSubmittedTrue(3L, 4L))
                .thenReturn(List.of(feedback()));
        when(userRepo.findByIdIn(setOf(2L, 7L))).thenReturn(setOf(student(), teacher()));
        when(feedbackRequestRepo.findByIdIn(setOf(4L))).thenReturn(setOf(feedbackRequest()));
        when(questionRepo.findByIdIn(setOf(10L))).thenReturn(setOf(question()));
        List<FeedbackDto> feedbackDtoList = feedbackService.getSubmittedFeedbackByFeedbackRequestId(3L, 4L);
        assertEquals(List.of(feedbackDto()), feedbackDtoList);
        verify(feedbackRepo).findByCourseIdAndFeedbackRequestIdAndSubmittedTrue(3L, 4L);
        verify(userRepo).findByIdIn(setOf(2L, 7L));
        verify(feedbackRequestRepo).findByIdIn(setOf(4L));
        verify(questionRepo).findByIdIn(setOf(10L));
    }

    @Test
    void testUpdateFeedbackAnswers() {
        when(feedbackRepo.findById(anyString())).thenReturn(Optional.ofNullable(feedback()));
        when(feedbackRepo.save(any())).thenReturn(feedback());
        List<AnswerDto> answerDtos = List.of(answerDto());
        List<Answer> answers = feedbackService.updateFeedbackAnswers(3L, 4L, "6025991aa9c08c230ed6f39a", answerDtos);
        assertEquals(answerDtos.size(), answers.size());
        verify(feedbackRepo, times(1)).findById(anyString());
        verify(feedbackRepo, times(1)).save(any());
    }

    @Test
    void testUpdateFeedbackAnswersNegativeCase() {
        when(feedbackRepo.findById(anyString())).thenReturn(Optional.empty());
        List<AnswerDto> answerDtos = List.of(answerDto());
        List<Answer> answers = feedbackService.updateFeedbackAnswers(3L, 4L, "6025991aa9c08c230ed6f39a", answerDtos);
        assertTrue(answers.isEmpty());
        verify(feedbackRepo, times(1)).findById(anyString());
        verify(feedbackRepo, times(0)).save(any());
    }

    @Test
    void testGetFeedbackCounterForUser() {
        when(feedbackRepo.findByUserIdAndCourseId(2L, 3L)).thenReturn(List.of(feedback()));
        FeedbackCounterDto result = feedbackService.getCounterForUser(2L, 3L);
        assertEquals(counterDtoForUser(), result);
        verify(feedbackRepo).findByUserIdAndCourseId(2L, 3L);
    }

    @Test
    void testGetFeedbackCounterForAdmin() {
        when(feedbackRepo.findByCourseId(3L)).thenReturn(List.of(feedback()));
        FeedbackCounterDto result = feedbackService.getCounterForAdmin(3L);
        assertEquals(counterDtoForAdmin(), result);
        verify(feedbackRepo).findByCourseId(3L);
    }

    private FeedbackRequest feedbackRequest() {
        return FeedbackRequest.builder()
                .id(4L)
                .active(true)
                .build();
    }

    private Feedback feedback() {
        return Feedback.builder()
                .id("6025991aa9c08c230ed6f39a")
                .userId(2L)
                .courseId(3L)
                .feedbackRequestId(4L)
                .active(true)
                .submitted(true)
                .answers(Set.of(answer()))
                .build();
    }

    private Answer answer() {
        return Answer.builder()
                .questionId(10L)
                .teacherId(7L)
                .comment("any comment")
                .rate(7)
                .build();
    }

    private AnswerDto answerDto() {
        return AnswerDto.builder()
                .questionId(question().getId())
                .question(question().getQuestionValue())
                .teacher(BriefUserDto.map(teacher()))
                .rate(answer().getRate())
                .comment(answer().getComment())
                .build();
    }

    private User student() {
        return User.builder()
                .id(2L)
                .role(Role.USER)
                .build();
    }

    private User teacher() {
        return User.builder()
                .id(7L)
                .role(Role.TEACHER)
                .build();
    }

    private Question question() {
        return Question.builder()
                .id(10L)
                .questionValue("question value")
                .pattern(true)
                .build();
    }

    private <T> Set<T> setOf(T... item) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, item);
        return set;
    }

    private FeedbackDto feedbackDto() {
        return map(List.of(feedback()),
                setOf(student(),
                        teacher()),
                setOf(feedbackRequest()),
                setOf(question())).get(0);
    }

    private FeedbackCounterDto counterDtoForUser() {
        return FeedbackCounterDto.builder()
                .newFeedback(0L)
                .activeFeedback(1L)
                .allFeedback(1)
                .build();
    }

    private FeedbackCounterDto counterDtoForAdmin() {
        return FeedbackCounterDto.builder()
                .activeFeedback(1L)
                .allFeedback(1)
                .notSubmittedFeedback(0L)
                .build();
    }
}
