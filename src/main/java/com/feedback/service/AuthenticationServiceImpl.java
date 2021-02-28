package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.exceptions.UserAlreadyExistException;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.User;
import org.hashids.Hashids;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private static final String SECRET_WORD = "id-secret";

    public AuthenticationServiceImpl(UserRepo userRepo, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (userRepo.findUserByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        User user = userRepo.saveAndFlush(UserDto.map(userDto, passwordEncoder.encode(userDto.getPassword())));
        sendRegistrationEmail(user);
        return UserDto.map(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public void confirmEmail(String id) {
        Hashids hashids = new Hashids(SECRET_WORD);
        User user = userRepo.findById(Long.parseLong(hashids.decodeHex(id))).orElseThrow(UserNotFoundException::new);
        user.setActive(true);
        userRepo.save(user);
    }

    private void sendRegistrationEmail(User user) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        Hashids hashids = new Hashids(SECRET_WORD);
        String id = hashids.encodeHex(user.getId().toString());

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("You are almost registered!");
        simpleMailMessage.setFrom("feedbackapplication.mail@gmail.com");
        simpleMailMessage.setText("Please click on the below link to activate your account. Thank you!" + "http://localhost:8080/api/auth/register/confirm/" + id);
        emailSenderService.sendEmail(simpleMailMessage);
    }

    @Override
    public void sendQuestionnaire(User user) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("form");
        simpleMailMessage.setFrom("feedbackapplication.mail@gmail.com");
        //user page is in process
        //simpleMailMessage.setText("please respond on a small questionnaire " + "http://localhost:8080/api/auth/findUserById/" + user.getId());
        simpleMailMessage.setText("please respond on a small questionnaire " + "http://feedback-server:8080/api/auth/findUserById/" + user.getId());
        emailSenderService.sendEmail(simpleMailMessage);
    }

}
