package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.exceptions.UserAlreadyExistException;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.feedback.dto.UserDto.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class UserServiceImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private static String SECRET_WORD = "id-secret";

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() { openMocks(this); }

    @AfterEach
    public void tearDown() { verifyNoMoreInteractions(userRepo, emailSenderService);}

    @Test()
    public void testRegisterNewUser() {
        UserDto dto = UserDto.builder()
                .firstName("test")
                .lastName("test")
                .email("test@mail.com")
                .password("test123")
                .phoneNumber("+3806602000800")
                .role(String.valueOf(Role.USER))
                .build();
        when(userRepo.findUserByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(userRepo.save(map(dto,dto.getPassword()))).thenReturn(user());
        UserDto userDto = userService.register(map(user()));
        assertNotNull(userDto);
        assertEquals(dto, userDto);
        verify(userRepo, times(1)).findUserByEmail("test@mail.com");
        verify(userRepo, times(1)).save(map(dto,user().getPassword()));
        verify(emailSenderService, times(1)).sendEmail(email());

    }

    @Test(expected = UserAlreadyExistException.class)
    public void testRegisterExistingUser() throws UserAlreadyExistException{
        when(userRepo.findUserByEmail("test@mail.com")).thenThrow(UserAlreadyExistException.class);
        verify(userRepo, times(1)).findUserByEmail("test@mail.com");
        verify(userRepo, times(0)).save(ArgumentMatchers.any());
        verify(emailSenderService, times(0)).sendEmail(ArgumentMatchers.any());

    }

    private User user(){
        return User.builder()
                .id(1l)
                .email("test@mail.com")
                .firstName("test")
                .lastName("test")
                .password(("test123"))
                .phoneNumber("+3806602000800")
                .role(Role.USER)
                .active(false)
                .build();
    }

    private SimpleMailMessage email() {
        SimpleMailMessage  email = new SimpleMailMessage();
        email.setTo(user().getEmail());
        email.setFrom("testFrom@mail.com");
        return email;
    }

}