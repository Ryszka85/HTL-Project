package com.ryszka.imageRestApi.errorHandling;

public class PrivateContentException extends RuntimeException{
    public PrivateContentException(String message) {
        super(message);
    }
}
