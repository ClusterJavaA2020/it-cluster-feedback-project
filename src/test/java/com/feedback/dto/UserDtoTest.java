package com.feedback.dto;

import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class UserDtoTest {

    @Test
   public void testMapUser() {
        UserDto userDto = UserDto.map(user());
        assertEquals(userDto, userDto());
    }

    @Test
    public void testMapUserDto() {
        User user = UserDto.map(userDto(), "12123");
        assertEquals(user, user());
    }

    private User user() {
        return User.builder()
                .id(1L)
                .email("some@example.com")
                .firstName("Some")
                .lastName("One")
                .password("12123")
                .phoneNumber("+380")
                .role(Role.USER)
                .build();
    }

    private UserDto userDto() {
        return UserDto.builder()
                .id(1L)
                .email("some@example.com")
                .firstName("Some")
                .lastName("One")
                .phoneNumber("+380")
                .role("USER")
                .build();
    }
}