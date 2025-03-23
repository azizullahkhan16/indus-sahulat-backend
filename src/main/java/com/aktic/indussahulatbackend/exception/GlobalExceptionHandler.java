package com.aktic.indussahulatbackend.exception;

import com.aktic.indussahulatbackend.exception.customexceptions.PatientNotFoundException;
import com.aktic.indussahulatbackend.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePatientNotFoundException(PatientNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),false), HttpStatus.NOT_FOUND);
    }

}

