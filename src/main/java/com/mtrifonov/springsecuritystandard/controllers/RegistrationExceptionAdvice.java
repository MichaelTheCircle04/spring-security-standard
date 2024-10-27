package com.mtrifonov.springsecuritystandard.controllers;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @Mikhail Trifonov
 */
@ControllerAdvice
public class RegistrationExceptionAdvice {
    
    @ExceptionHandler(RegistrationException.class)
    public /*String*/ResponseEntity<Map<String, String>> handleIllegalArgumentException(/*Model model, */RegistrationException e) {
        /*String message = e.getMessage();
        model.addAttribute("exception", message);
        return "registration";*/
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(Map.of("exception", e.getMessage()));
    }
}
