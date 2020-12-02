package com.ryszka.imageRestApi.errorHandling;

import com.ryszka.imageRestApi.viewModels.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AddressNotFoundExceptionHandler {
    @ExceptionHandler(value = {AddressNotFoundException.class})
    public ResponseEntity<Object> handlePersistenceException(EntityPersistenceException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
