package com.ryszka.imageRestApi.errorHandling;

import com.ryszka.imageRestApi.viewModels.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class BadConnectionErrorHandler {
    @ExceptionHandler(value = {BadConnectionException.class})
    public ResponseEntity<Object> handleBadConnectionException(EntityNotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
