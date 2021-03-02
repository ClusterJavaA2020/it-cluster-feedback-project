package com.feedback.controller;

import com.feedback.exceptions.CourseNotFoundException;
import com.feedback.exceptions.QuestionNotFoundException;
import com.feedback.model.AdviceModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return AdviceModel.builder().status(HttpStatus.NOT_FOUND).message(e.getMessage()).build();
    }
}
