package com.feedback.repo;

import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepoTest {
    @Autowired
    UserRepo userRepo;

    @Test
    void testSave() {
        User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .email("email@mail.com")
                .password("12345_password")
                .phoneNumber("0987654321")
                .role(Role.USER)
                .build();
        userRepo.save(user);
        Optional<User> byId = userRepo.findById(user.getId());
        assertTrue(byId.isPresent());
        byId.get().setCourses(null);
        byId.get().setFeedbackRequests(null);
        assertEquals(user, byId.get());
    }

}