package com.ryszka.imageRestApi.errorHandling;

public class UserRegistrationFailedException extends RuntimeException{

    public UserRegistrationFailedException(String message) {
        super(message);
    }
}
