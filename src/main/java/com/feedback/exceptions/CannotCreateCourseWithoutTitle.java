package com.feedback.exceptions;

public class CannotCreateCourseWithoutTitle extends RuntimeException{
    private static final String MESSAGE = "you must enter at least title";
    public CannotCreateCourseWithoutTitle(){super(MESSAGE);}
}
