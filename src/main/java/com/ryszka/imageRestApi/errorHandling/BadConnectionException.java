package com.ryszka.imageRestApi.errorHandling;

public class BadConnectionException extends RuntimeException{
    private static final long serialVersionUID = -4246523295365725478L;

    public BadConnectionException(String message) {
        super(message);
    }
}
