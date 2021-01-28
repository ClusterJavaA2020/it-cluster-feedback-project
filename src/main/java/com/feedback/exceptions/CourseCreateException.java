package com.feedback.exceptions;

public class CourseCreateException extends RuntimeException{
    private static final String MESSAGE = "you must enter at least title";
    public CourseCreateException(){super(MESSAGE);}
}
