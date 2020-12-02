package com.ryszka.imageRestApi.errorHandling.handler;

import com.ryszka.imageRestApi.errorHandling.EntityAccessNotAllowedException;
import com.ryszka.imageRestApi.errorHandling.ScalingImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntityAccessNotAllowedHandler {

    @ExceptionHandler(value = {EntityAccessNotAllowedException.class})
    public ResponseEntity<Object> handleRegistrationError(EntityAccessNotAllowedException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());

    }
}
