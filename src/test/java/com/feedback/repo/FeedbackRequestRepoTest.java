package com.feedback.repo;

import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FeedbackRequestRepoTest {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FeedbackRequestRepo feedbackRequestRepo;
    @Autowired
    private CourseRepo courseRepo;

    @Test
    void testSave() {
        User user = userRepo.save(User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .email("email.mail.com")
                .role(Role.ADMINISTRATOR)
                .build());
        Course testCourse = courseRepo.save(Course.builder().
                title("Test course")
                .description("Test description")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .build());
        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .course(testCourse)
                .users(Set.of(user))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .isActive(true)
                .build();
        feedbackRequestRepo.save(feedbackRequest);
        Optional<FeedbackRequest> byId = feedbackRequestRepo.findById(feedbackRequest.getId());
        assertTrue(byId.isPresent());
        byId.get().setUsers(Set.of(user));
        byId.get().setCourse(testCourse);
        assertEquals(feedbackRequest, byId.get());
    }
}