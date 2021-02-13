package com.feedback.dto;

import com.feedback.model.Answer;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.feedback.dto.FeedbackDto.map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeedbackDtoTest {

    @Test
    void testMap() {
        List<FeedbackDto> feedbackDtoList =
                FeedbackDto.map(List.of(feedback()), setOf(student(), teacher()), setOf(feedbackRequest()), setOf(question()));
        assertEquals(feedbackDtoList.get(0), feedbackDto());
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

//    private AnswerDto answerDto() {
//        return AnswerDto.builder()
//                .questionId(question().getId())
//                .question(question().getQuestionValue())
//                .teacher(BriefUserDto.map(teacher()))
//                .rate(answer().getRate())
//                .comment(answer().getComment())
//                .build();
//    }

    private User student() {
        return User.builder()
                .id(2L)
                .role(Role.USER)
                .email("student@mail.com")
                .courses(Set.of(course()))
                .build();
    }

    private User teacher() {
        return User.builder()
                .id(7L)
                .role(Role.TEACHER)
                .email("teacher@mail.com")
                .courses(Set.of(course()))
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

    private Course course() {
        return Course.builder()
                .id(100L)
                .title("Test course")
                .description("Some description")
                .active(true)
                .build();
    }
}
