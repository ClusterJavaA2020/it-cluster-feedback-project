package com.feedback.exceptions;

public class UserNotFoundException extends RuntimeException{
    private static final String MESSAGE = "This user doesn't exist";
    public UserNotFoundException(){super(MESSAGE);}
}
