package com.mtrifonov.springsecuritystandard.controllers;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @Mikhail Trifonov
 */
@ControllerAdvice
public class RegistrationExceptionAdvice {
    
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(RegistrationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(Map.of("exception", e.getMessage()));
    }
}
