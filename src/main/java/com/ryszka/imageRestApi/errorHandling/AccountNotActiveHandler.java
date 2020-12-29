package com.ryszka.imageRestApi.errorHandling;

import com.ryszka.imageRestApi.viewModels.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AccountNotActiveHandler {
    @ExceptionHandler(value = {AccountNotActiveException.class})
    public ResponseEntity<Object> handleAccountNotActiveException(AccountNotActiveException exception, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.CONFLICT);
    }
}
