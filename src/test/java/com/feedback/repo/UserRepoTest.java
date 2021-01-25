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
    private UserRepo userRepo;

    @Test
    void testSave() {
        User user = user();
        user.setEmail("email@mail.com");
        user.setRole(Role.USER);
        userRepo.save(user);
        Optional<User> byId = userRepo.findById(user.getId());
        assertTrue(byId.isPresent());
        byId.get().setCourses(null);
        assertEquals(user, byId.get());
    }

    @Test
    void testFindTeacherById() {
        User user = user();
        user.setEmail("teacher@mail.com");
        user.setRole(Role.TEACHER);
        userRepo.save(user);
        Optional<User> teacher = userRepo.findTeacherById(user.getId());
        assertTrue(teacher.isPresent());
        assertEquals(user, teacher.get());
    }

    @Test
    void testFindUserByEmail() {
        User user = user();
        user.setEmail("some.email@mail.com");
        user.setRole(Role.ADMINISTRATOR);
        userRepo.save(user);
        Optional<User> admin = userRepo.findUserByEmail(user.getEmail());
        assertTrue(admin.isPresent());
        assertEquals(user, admin.get());

    }

    private User user() {
        return User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .password("12345_password")
                .phoneNumber("0987654321")
                .build();
    }
}
