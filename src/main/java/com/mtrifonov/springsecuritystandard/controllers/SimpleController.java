package com.mtrifonov.springsecuritystandard.controllers;

import com.mtrifonov.springsecuritystandard.UserRepresentation;
import com.mtrifonov.springsecuritystandard.services.RegistrationService;
import com.mtrifonov.springsecuritystandard.validators.RegistrationRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 *
 * @Mikhail Trifonov
 */
@Controller
public class SimpleController {
    
    private final RegistrationService service;
    private final RegistrationRequestValidator validator;
    
    @Autowired
    public SimpleController(RegistrationService service, RegistrationRequestValidator validator) {
        this.service = service;
        this.validator = validator;
    }
   
    @GetMapping("/login")
    public String produceLoginPage() {
        return "login";
    }   
    
    @GetMapping("/api")
    public String produceApiPage() {
        return "api";
    }  
    
    @GetMapping("/registration")
    public String produceRegistrationPage() {
        return "registration";
    }
    
    @PostMapping("/registration")
    public ResponseEntity<Void> register(@RequestBody UserRepresentation user) {       
        validator.validate(user); 
        service.register(user);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).build();
    }
}
