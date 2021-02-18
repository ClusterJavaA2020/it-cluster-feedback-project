package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.exceptions.UserAlreadyExistException;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.hashids.Hashids;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static com.feedback.dto.UserDto.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationServiceImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(userRepo, emailSenderService);
    }

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
        when(passwordEncoder.encode(any())).thenReturn("test123");
        when(userRepo.saveAndFlush(map(dto, (dto.getPassword())))).thenReturn(user());
        UserDto userDto = authenticationService.register(map(user()));
        assertNotNull(userDto);
        assertEquals(map(user()), userDto);
        verify(userRepo, times(1)).findUserByEmail("test@mail.com");
        verify(userRepo, times(1)).saveAndFlush(map(dto, user().getPassword()));
        verify(emailSenderService, times(1)).sendEmail(email());
    }

    @Test(expected = UserAlreadyExistException.class)
    public void testRegisterExistingUser() throws UserAlreadyExistException {
        UserDto dto = UserDto.builder()
                .firstName("test")
                .lastName("test")
                .email("test@mail.com")
                .password("test123")
                .phoneNumber("+3806602000800")
                .role(String.valueOf(Role.USER))
                .build();
        when(userRepo.findUserByEmail("test@mail.com")).thenReturn(Optional.of(user()));
        authenticationService.register(dto);
        User user = userRepo.findUserByEmail("test@mail.com").get();
        assertNotNull(user);
        verify(userRepo, times(1)).findUserByEmail("test@mail.com");
        verify(userRepo, times(0)).saveAndFlush(any());
        verify(emailSenderService, times(0)).sendEmail(any());
    }

    @Test
    public void testSendRegistrationEmail() {
        emailSenderService.sendEmail(email());
        verify(emailSenderService, times(1)).sendEmail(email());
    }

    private User user() {
        return User.builder()
                .id(1L)
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
        SimpleMailMessage email = new SimpleMailMessage();
        Hashids hashids = new Hashids("id-secret");
        String id = hashids.encodeHex(user().getId().toString());
        email.setTo(user().getEmail());
        email.setFrom("feedbackapplication.mail@gmail.com");
        email.setSubject("You are almost registered!");
        email.setText("Please click on the below link to activate your account. Thank you!" + "http://localhost:8080/auth/register/confirm/" + id);
        return email;
    }

}
