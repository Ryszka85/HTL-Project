package com.ryszka.imageRestApi.errorHandling;

public class ScalingImageException extends RuntimeException{
    public ScalingImageException(String message) {
        super(message);
    }
}
