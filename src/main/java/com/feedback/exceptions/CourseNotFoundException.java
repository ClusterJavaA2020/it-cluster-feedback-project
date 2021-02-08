package com.feedback.exceptions;

public class CourseNotFoundException extends RuntimeException {
    private static final String MESSAGE = "this course doesn't exist";
    public CourseNotFoundException() {
        super(MESSAGE);
    }
}
