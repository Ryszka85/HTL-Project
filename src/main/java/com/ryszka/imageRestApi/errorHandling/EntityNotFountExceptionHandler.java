package com.ryszka.imageRestApi.errorHandling;

import com.ryszka.imageRestApi.viewModels.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class EntityNotFountExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleImageNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
