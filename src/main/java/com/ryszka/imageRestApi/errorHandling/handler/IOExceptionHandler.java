package com.ryszka.imageRestApi.errorHandling.handler;

import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.ImageToSmallException;
import com.ryszka.imageRestApi.errorHandling.ScalingImageException;
import com.ryszka.imageRestApi.errorHandling.UserRegistrationFailedException;
import com.ryszka.imageRestApi.viewModels.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class IOExceptionHandler {

        @ExceptionHandler(value = {ScalingImageException.class})
        public ResponseEntity<Object> handleRegistrationError(ScalingImageException exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());

        }

}
