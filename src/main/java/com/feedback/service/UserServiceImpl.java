package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.exceptions.UserAlreadyExistException;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;



@Service
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

  /*  public void register(UserDto registrationDto) {
        User user = User.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(Role.USER)
                .build();
        userRepo.save(user);
    }*/

    @Override
    public UserDto register(UserDto userDto){
        if(userRepo.findUserByEmail(userDto.getEmail()).isPresent()){
            throw new UserAlreadyExistException();
        }

        User user = userRepo.saveAndFlush(UserDto.map(userDto,passwordEncoder.encode(userDto.getPassword())));
        sendRegistrationEmail(user.getEmail());
        return UserDto.map(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

   /* @Override
    public UserDto update(UserDto userDto) {
        if(userRepo.findUserByEmail(userDto.getEmail()).isEmpty()){
            throw new UserNotFoundException();
        }
        return map(userRepo.save(map(userDto)));
    }*/

    public void confirmEmail(String email){
       Optional<User> user = userRepo.findUserByEmail(email);
       User existedUser = user.orElseThrow(UserNotFoundException::new);
       existedUser.setActive(true);
       userRepo.save(existedUser);
    }

    private void sendRegistrationEmail(String userMail){
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userMail);
        simpleMailMessage.setSubject("You are almost registered!");
        simpleMailMessage.setFrom("feedbackapplication.mail@gmail.com");
        simpleMailMessage.setText("Please click on the below link to activate your account. Thank you!" + "http://localhost:8080/api/auth/confirm?email="+userMail);
        emailSenderService.sendEmail(simpleMailMessage);

    }
}
