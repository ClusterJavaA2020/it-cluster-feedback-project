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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

    @Test
    public void testFindByEmail() {
        when(userRepo.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(user()));
        Optional<User> user = authenticationService.findByEmail(anyString());
        assertEquals(user().getEmail(), user.get().getEmail());
        verify(userRepo).findUserByEmail(anyString());
    }

    @Test
    public void testSendRegistrationEmail() {
        emailSenderService.sendEmail(email());
        verify(emailSenderService, times(1)).sendEmail(email());
    }

    @Test
    public void testRegister() {
        UserDto userDto = map(user());
        when(userRepo.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.saveAndFlush(any())).thenReturn(UserDto.map(userDto, "test123"));
        doNothing().when(emailSenderService).sendEmail(any(SimpleMailMessage.class));
        UserDto result = authenticationService.register(userDto);
        userDto.setPassword(null);
        assertEquals(result, userDto);
        verify(userRepo, times(1)).findUserByEmail(anyString());
        verify(userRepo, times(1)).saveAndFlush(any());
        verify(emailSenderService, times(1)).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    public void testRegisterNegativeCase() {
        UserDto userDto = map(user());
        when(userRepo.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(UserDto.map(userDto, "12123")));
        assertThrows(UserAlreadyExistException.class, () -> authenticationService.register(userDto));
        verify(userRepo, times(1)).findUserByEmail(anyString());
        verify(userRepo, times(0)).saveAndFlush(any());
        verify(emailSenderService, times(0)).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    public void testConfirmEmail() {
        Hashids hashids = new Hashids("id-secret");
        String id = hashids.encodeHex(user().getId().toString());
        when(userRepo.findById(1l)).thenReturn(Optional.ofNullable(user()));
        when(userRepo.save(user())).thenReturn(user());
        authenticationService.confirmEmail(id);
        assertEquals(user().isActive(), false);
        verify(userRepo, times(1)).findById(user().getId());
        verify(userRepo, times(1)).save(user());
    }

    @Test
    public void testSendQuestionnaire() {
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
        email.setText("Please click on the below link to activate your account. Thank you!" + "http://localhost:8080/api/auth/register/confirm/" + id);
        return email;
    }

}
