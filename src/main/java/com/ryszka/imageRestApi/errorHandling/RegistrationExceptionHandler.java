package com.ryszka.imageRestApi.errorHandling;

import com.ryszka.imageRestApi.viewModels.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegistrationExceptionHandler {
    @ExceptionHandler(value = {UserRegistrationFailedException.class})
    public ResponseEntity<Object> handleRegistrationError(UserRegistrationFailedException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
