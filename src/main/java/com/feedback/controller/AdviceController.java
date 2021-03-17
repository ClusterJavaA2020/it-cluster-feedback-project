package com.feedback.controller;

import com.feedback.exceptions.CourseNotFoundException;
import com.feedback.exceptions.QuestionNotFoundException;
import com.feedback.exceptions.UserAlreadyExistException;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.model.AdviceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice()
public class AdviceController {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {CourseNotFoundException.class})
    public AdviceModel courseNotFound(CourseNotFoundException e) {
        return AdviceModel.builder().status(HttpStatus.NOT_FOUND).message(e.getMessage()).build();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {QuestionNotFoundException.class})
    public AdviceModel questionNotFoundException(QuestionNotFoundException e) {
        log.error("Question not found");
        return AdviceModel.builder().status(HttpStatus.NOT_FOUND).message(e.getMessage()).build();
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public AdviceModel userAlreadyExistException(UserAlreadyExistException e) {
        log.error("User already exist");
        return AdviceModel.builder().status(HttpStatus.CONFLICT).message(e.getMessage()).build();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {UserNotFoundException.class})
    public AdviceModel userNotFoundException(UserNotFoundException e) {
        log.error("User not found");
        return AdviceModel.builder().status(HttpStatus.NOT_FOUND).message(e.getMessage()).build();
    }
}
