package com.ryszka.imageRestApi.errorHandling;


public class AccountNotActiveException extends RuntimeException{
    public AccountNotActiveException(String message) {
        super(message);
    }
}
