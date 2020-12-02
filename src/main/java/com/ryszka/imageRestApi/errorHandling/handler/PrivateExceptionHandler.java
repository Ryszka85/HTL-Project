package com.ryszka.imageRestApi.errorHandling.handler;

import com.ryszka.imageRestApi.errorHandling.PrivateContentException;
import com.ryszka.imageRestApi.errorHandling.ScalingImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PrivateExceptionHandler {
    @ExceptionHandler(value = {PrivateContentException.class})
    public ResponseEntity<Object> handleRegistrationError(PrivateContentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());

    }
}
