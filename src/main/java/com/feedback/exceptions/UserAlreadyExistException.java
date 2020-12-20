package com.feedback.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    private static final String MESSAGE = "This user already exist";
    public UserAlreadyExistException(){super(MESSAGE);}
}
