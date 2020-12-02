package com.ryszka.imageRestApi.errorHandling.handler;

import com.ryszka.imageRestApi.errorHandling.ImageToSmallException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ImageToSmallExceptionHandler {
    @ExceptionHandler(value = {ImageToSmallException.class})
    public ResponseEntity<Object> imageToSmallError(ImageToSmallException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());

    }
}
