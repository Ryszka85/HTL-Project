package com.ryszka.imageRestApi.errorHandling;

public class EmailAlreadySentException extends RuntimeException{
    public EmailAlreadySentException(String message) {
        super(message);
    }
}
