package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.BriefUserDto;
import com.feedback.model.Answer;
import com.feedback.repo.FeedbackAnswersRepo;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.FeedbackAnswers;
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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static com.feedback.service.FeedbackRequestServiceImpl.END_DATE;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
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
    @Mock
    private FeedbackAnswersRepo feedbackAnswersRepo;

    @InjectMocks
    private AnswerServiceImpl answerService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(userRepo, questionRepo, feedbackRepo, feedbackRequestRepo, feedbackAnswersRepo);
    }

    @Test
    void testCreateAnswer() {
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.ofNullable(feedbackRequest()));
        when(feedbackAnswersRepo.findByFeedbackRequestId(1L)).thenReturn(feedbackAnswers());
        when(questionRepo.findById(10L)).thenReturn(Optional.ofNullable(question()));
        when(userRepo.findTeacherById(147L)).thenReturn(Optional.ofNullable(user()));
        when(feedbackAnswersRepo.save(feedbackAnswers(answer()))).thenReturn(feedbackAnswers(answer()));
        Answer answer = answerService.createAnswer(1L, 1L, answer());
        assertNotNull(answer);
        assertEquals(answer(), answer);
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackAnswersRepo).findByFeedbackRequestId(1L);
        verify(questionRepo).findById(10L);
        verify(userRepo).findTeacherById(147L);
        verify(feedbackAnswersRepo).save(feedbackAnswers(answer()));
    }

    @Test
    void testCreateAnswerWithEmptyTeacher() {
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.ofNullable(feedbackRequest()));
        when(feedbackAnswersRepo.findByFeedbackRequestId(anyLong())).thenReturn(feedbackAnswers());
        when(questionRepo.findById(anyLong())).thenReturn(Optional.ofNullable(question()));
        when(userRepo.findTeacherById(anyLong())).thenReturn(Optional.empty());
        Answer answer = answerService.createAnswer(1L, 1L, answer());
        assertNull(answer);
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackAnswersRepo, times(1)).findByFeedbackRequestId(anyLong());
        verify(questionRepo, times(1)).findById(anyLong());
        verify(userRepo, times(1)).findTeacherById(anyLong());
    }

    @Test
    void testCreateAnswerWithInvalidParams() {
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.ofNullable(feedbackRequest()));
        when(feedbackAnswersRepo.findByFeedbackRequestId(anyLong())).thenReturn(feedbackAnswers());
        when(questionRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepo.findTeacherById(anyLong())).thenReturn(Optional.ofNullable(user()));
        Answer answer = answerService.createAnswer(2L, 1L, answer());
        assertNull(answer);
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackAnswersRepo, times(1)).findByFeedbackRequestId(anyLong());
        verify(questionRepo, times(1)).findById(anyLong());
        verify(userRepo, times(1)).findTeacherById(anyLong());
    }

    @Test
    void testGetAnswersByFeedbackRequestId() {
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.of(feedbackRequest()));
        when(feedbackAnswersRepo.findByFeedbackRequestId(1L)).thenReturn(feedbackAnswersFilled());
        when(questionRepo.findById(10L)).thenReturn(Optional.ofNullable(question()));
        when(userRepo.findTeacherById(147L)).thenReturn(Optional.ofNullable(user()));
        Set<AnswerDto> answerDtoResult = answerService.getAnswersByFeedbackRequestId(1L, 1L);
        assertNotNull(answerDtoResult);
        answerDtoResult.forEach(item -> {
            assertEquals(question().getId(), item.getQuestionId());
            assertEquals(question().getQuestionValue(), item.getQuestion());
            assertEquals(BriefUserDto.map(user()), item.getTeacher());
            assertEquals(answer().getRate(), item.getRate());
            assertEquals(answer().getComment(), item.getComment());
        });
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackAnswersRepo).findByFeedbackRequestId(1L);
        verify(questionRepo).findById(10L);
        verify(userRepo).findTeacherById(147L);
    }

    @Test
    void testGetAnswersByFeedbackRequestIdWithInvalidParams() {
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.of(feedbackRequest()));
        Set<AnswerDto> answerDtoResult = answerService.getAnswersByFeedbackRequestId(2L, 1L);
        assertTrue(answerDtoResult.isEmpty());
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackAnswersRepo, times(0)).findByFeedbackRequestId(anyLong());
        verify(questionRepo, times(0)).findById(anyLong());
        verify(userRepo, times(0)).findTeacherById(anyLong());
    }

    @Test
    void testGetAnswersByFeedbackRequestIdEmptyFeedback() {
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.of(feedbackRequest()));
        when(feedbackAnswersRepo.findByFeedbackRequestId(1L)).thenReturn(null);
        Set<AnswerDto> answerDtoResult = answerService.getAnswersByFeedbackRequestId(1L, 1L);
        assertTrue(answerDtoResult.isEmpty());
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackAnswersRepo, times(1)).findByFeedbackRequestId(anyLong());
        verify(questionRepo, times(0)).findById(anyLong());
        verify(userRepo, times(0)).findTeacherById(anyLong());
    }

    @Test
    void testDeleteAnswer() {
        when(feedbackAnswersRepo.findByFeedbackRequestId(1L)).thenReturn(feedbackAnswers(answer()));
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.ofNullable(feedbackRequest()));
        when(feedbackAnswersRepo.save(feedbackAnswers())).thenReturn(feedbackAnswers());
        Set<Answer> answerSet = answerService.deleteAnswer(1L, 1L, answer());
        assertNotNull(answerSet);
        assertEquals(Collections.emptySet(), answerSet);
        verify(feedbackAnswersRepo).findByFeedbackRequestId(1L);
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackAnswersRepo).save(feedbackAnswers());
    }

    @Test
    void testDeleteAnswerWithInvalidParams() {
        when(feedbackAnswersRepo.findByFeedbackRequestId(anyLong())).thenReturn(feedbackAnswers(answer()));
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.ofNullable(feedbackRequest()));
        Set<Answer> answerSet = answerService.deleteAnswer(2L, 1L, answer());
        assertTrue(answerSet.isEmpty());
        verify(feedbackAnswersRepo, times(1)).findByFeedbackRequestId(anyLong());
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackAnswersRepo, times(0)).save(any());
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
                .pattern(true)
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
                .build();
    }

    private Answer answer() {
        return Answer.builder()
                .questionId(10L)
                .teacherId(147L)
                .rate(5)
                .comment("some comment")
                .build();
    }

    private FeedbackAnswers feedbackAnswers() {
        return FeedbackAnswers.builder()
                .id("600cd3b5cbeda2185b994b0b")
                .feedbackRequestId(1L)
                .answers(new LinkedHashSet<>())
                .build();
    }

    private FeedbackAnswers feedbackAnswersFilled() {
        FeedbackAnswers feedbackAnswers = feedbackAnswers();
        feedbackAnswers.getAnswers().add(answer());
        return feedbackAnswers;
    }

    private FeedbackAnswers feedbackAnswers(Answer answer) {
        FeedbackAnswers feedbackAnswers = feedbackAnswers();
        feedbackAnswers.getAnswers().add(answer);
        return feedbackAnswers;
    }

}
