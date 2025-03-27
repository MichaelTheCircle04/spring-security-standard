package com.mtrifonov.springsecuritystandard.exceptions;

import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @Mikhail Trifonov
 */
@ControllerAdvice
public class BaseAdvice {
    
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, String>> handleRegistrationException(RegistrationException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }

    @ExceptionHandler(RoleChangeException.class)
    public ResponseEntity<Map<String, String>> handleRoleChangeException(RoleChangeException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }
}
