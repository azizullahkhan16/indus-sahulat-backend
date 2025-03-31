package com.aktic.indussahulatbackend.exception;

import com.aktic.indussahulatbackend.exception.customexceptions.*;
import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.PatientNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.QuestionNotFoundException;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePatientNotFoundException(PatientNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),false), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQuestionNotFoundException(QuestionNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),false), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDriverNotFoundException(DriverNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),false), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AmbulanceProviderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAmbulanceProviderNotFoundException(AmbulanceProviderNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),false), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AmbulanceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAmbulanceNotFoundException(AmbulanceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),false), HttpStatus.NOT_FOUND);
    }


    // Handle generic exceptions (unexpected errors)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
//        ApiResponse<String> response = new ApiResponse<>(false, "An unexpected error occurred: " + ex.getMessage(), null);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}

