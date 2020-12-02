package com.ryszka.imageRestApi.errorHandling;

public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }
}
