package com.ryszka.imageRestApi.errorHandling;

public class EntityNotFoundException extends RuntimeException{

    private static final long serialVersionUID = -2093737453636713881L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
