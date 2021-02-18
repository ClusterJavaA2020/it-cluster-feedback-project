package com.feedback.service;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class EmailSenderServiceTest {

    @Test
    public void testSendEmail() {
        EmailSenderService emailSenderService = mock(EmailSenderService.class);
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo("testTo@mail.com");
        email.setFrom("testFrom@mail.com");
        emailSenderService.sendEmail(email);
        verify(emailSenderService, times(1)).sendEmail(email);
    }
}