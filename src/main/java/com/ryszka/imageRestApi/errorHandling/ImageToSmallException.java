package com.ryszka.imageRestApi.errorHandling;

public class ImageToSmallException extends RuntimeException{
    public ImageToSmallException(String message) {
        super(message);
    }
}
