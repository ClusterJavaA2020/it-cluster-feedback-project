package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.model.Answer;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
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
import java.util.Set;

import static com.feedback.service.FeedbackRequestServiceImpl.END_DATE;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class FeedbackRequestServiceTest {

    @Mock
    FeedbackRequestRepo feedbackRequestRepo;
    @Mock
    CourseRepo courseRepo;
    @Mock
    FeedbackRepo feedbackRepo;
    @Mock
    QuestionRepo questionRepo;

    @InjectMocks
    FeedbackRequestServiceImpl feedbackRequestServiceImpl;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(feedbackRequestRepo);
        verifyNoMoreInteractions(questionRepo);
        setOfUsers().forEach(user -> verifyNoMoreInteractions(feedbackRepo));
    }

    @Test
    void testCreateFeedbackRequest() {
        when(courseRepo.findById(1L)).thenReturn(ofNullable(course()));
        when(feedbackRequestRepo.save(feedbackRequest())).thenReturn(feedbackRequestDB());
        when(questionRepo.findByIsPatternTrue()).thenReturn(setOfQuestions());
        setOfUsers().forEach(user -> when(feedbackRepo.save(feedback(user))).thenReturn(feedbackDB(user)));
        // NOT_NULL
        FeedbackRequestDto requestDto = feedbackRequestServiceImpl.createFeedbackRequest(1L);
        assertNotNull(requestDto);
        assertEquals(requestDto.getCourseTitle(), feedbackRequest().getCourse().getTitle());
        assertEquals(requestDto.getStartDate(), feedbackRequest().getStartDate());
        assertEquals(requestDto.getEndDate(), feedbackRequest().getEndDate());
        assertEquals(requestDto.getId(), feedbackRequestDB().getId());
        // NULL
        FeedbackRequestDto requestDtoNull = feedbackRequestServiceImpl.createFeedbackRequest(2L);
        assertNull(requestDtoNull);

        verify(feedbackRequestRepo).save(feedbackRequest());
        verify(questionRepo).findByIsPatternTrue();
        setOfUsers().forEach(user -> verify(feedbackRepo).save(feedback(user)));

    }

    private Set<User> setOfUsers() {
        return Set.of(
                User.builder()
                        .id(1L)
                        .firstName("FirstUserName")
                        .lastName("FirstLastName")
                        .email("first@mail.com")
                        .role(Role.USER)
                        .password("12345")
                        .feedbackRequests(new HashSet<>())
                        .build(),
                User.builder()
                        .id(2L)
                        .firstName("SecondUserName")
                        .lastName("SecondLastName")
                        .email("second@mail.com")
                        .role(Role.USER)
                        .password("password")
                        .feedbackRequests(new HashSet<>())
                        .build()
        );
    }

    private Course course() {
        return Course.builder()
                .id(1L)
                .title("Mock course")
                .description("This is the test course.")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(4))
                .users(setOfUsers())
                .build();
    }

    private FeedbackRequest feedbackRequest() {
        return FeedbackRequest.builder()
                .course(course())
                .users(setOfUsers())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(END_DATE))
                .build();
    }

    private FeedbackRequest feedbackRequestDB() {
        FeedbackRequest feedbackRequest = feedbackRequest();
        feedbackRequest.setId(1L);
        return feedbackRequest;
    }

    private Set<Question> setOfQuestions() {
        return Set.of(
                Question.builder()
                        .id(1L)
                        .isPattern(true)
                        .questionValue("First question")
                        .build(),
                Question.builder()
                        .id(2L)
                        .isPattern(true)
                        .questionValue("Second question")
                        .build()
        );
    }

    Set<Answer> setOfAnswers() {
        Set<Answer> setOfAnswers = new HashSet<>();
        setOfQuestions().forEach(
                q -> setOfAnswers.add(Answer.builder()
                        .questionId(q.getId())
                        .build()));
        return setOfAnswers;
    }

    private Feedback feedback(User user) {
        return Feedback.builder()
                .isClosed(false)
                .feedbackRequestId(feedbackRequestDB().getId())
                .userId(user.getId())
                .isClosed(false)
                .answer(setOfAnswers())
                .answer(setOfAnswers())
                .build();
    }

    private Feedback feedbackDB(User user) {
        Feedback feedback = feedback(user);
        feedback.setId("someId");
        return feedback;
    }

}