package com.ryszka.imageRestApi.errorHandling;

public class EntityPersistenceException extends RuntimeException{
    private static final long serialVersionUID = 5390400786654801378L;

    public EntityPersistenceException(String message) {
        super(message);
    }
}
