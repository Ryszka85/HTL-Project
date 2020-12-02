package com.ryszka.imageRestApi.errorHandling;

public class EntityAccessNotAllowedException extends RuntimeException{
    public EntityAccessNotAllowedException(String message) {
        super(message);
    }
}
