package com.mtrifonov.springsecuritystandard.controllers;

import com.mtrifonov.springsecuritystandard.UserDTO;
import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import com.mtrifonov.springsecuritystandard.services.RegistrationService;
import com.mtrifonov.springsecuritystandard.validators.RegistrationRequestValidator;

import lombok.AllArgsConstructor;

import java.net.URI;
import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 *
 * @Mikhail Trifonov
 */
@Controller
@AllArgsConstructor
public class MainController {
    
    private final RegistrationService service;
    private final UserRepository userRepository;
    private final RegistrationRequestValidator validator;
     
    @GetMapping("/main")
    public String produceMainPage() {
        return "main";
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) throws SQLException {
        var user = userRepository.findUserById(id).get();
        return ResponseEntity.ok().body(user);
    }
    
    @GetMapping("/user")
    public String produceUserPage() {
        return "user";
    }  
    
    @GetMapping("/login")
    public String produceLoginPage() {
        return "login";
    } 
    
    @GetMapping("/registration")
    public String produceRegistrationPage() {
        return "registration";
    }
    
    @PostMapping("/registration")
    public ResponseEntity<Void> register(@RequestBody UserDTO user) throws SQLException {  
        validator.validate(user); 
        service.register(new UserDTO[] {user});
        var created = userRepository.findUserByUsername(user.getUsername()).get();
        return ResponseEntity.created(URI.create("http://localhost:8080/users/" + created.getId())).build();
    }
}
