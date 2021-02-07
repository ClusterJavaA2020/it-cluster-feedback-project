package com.feedback.exceptions;

public class QuestionNotFoundException extends RuntimeException {
    private static final String MESSAGE = "there is no question with this id";
    public QuestionNotFoundException() {
        super(MESSAGE);
    }
}
