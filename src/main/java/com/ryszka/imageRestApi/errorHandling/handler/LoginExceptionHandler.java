package com.ryszka.imageRestApi.errorHandling.handler;

import com.ryszka.imageRestApi.errorHandling.LoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoginExceptionHandler {
    @ExceptionHandler(value = {LoginException.class})
    public ResponseEntity<Object> handleLoginError(LoginException loginException) {
        return ResponseEntity
                .status(401)
                .body(loginException.getMessage());
    }
}
