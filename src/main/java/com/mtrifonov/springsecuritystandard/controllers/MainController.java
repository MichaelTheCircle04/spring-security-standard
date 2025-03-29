package com.mtrifonov.springsecuritystandard.controllers;

import com.mtrifonov.springsecuritystandard.UserDTO;
import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import com.mtrifonov.springsecuritystandard.services.RegistrationService;
import com.mtrifonov.springsecuritystandard.validators.RegistrationRequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static com.mtrifonov.springsecuritystandard.Role.*;
import java.net.URI;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequiredArgsConstructor
public class MainController {
    
    private final RegistrationService service;
    private final UserRepository userRepository;
    private final RegistrationRequestValidator validator;
    @Value("${server.name}")
    private String address;
     
    @GetMapping("/main") 
    public String produceMainPage() {
        return "main";
    }

    @GetMapping("/users/{id}") 
    public ResponseEntity<UserDTO> getUserById(@AuthenticationPrincipal UserDetails requester, @PathVariable Integer id) throws SQLException {
        
        var user = userRepository.findUserById(id).get();
        checkAccess(requester, user);
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
    public ResponseEntity<Void> register(@Valid @RequestBody UserDTO user) throws SQLException {  

        validator.validate(user); 
        service.register(new UserDTO[] {user});
        var created = userRepository.findUserByUsername(user.getUsername()).get();
        return ResponseEntity.created(URI.create("http://" + address + "/users/" + created.getId())).build();
    }

    private void checkAccess(UserDetails requester, UserDTO user) throws SQLException {

        if (requester.getUsername().equals(user.getUsername())) {
            return;
        }

        if (greatestRole(requester.getAuthorities()).majority <= greatestRole(user.getRoles()).majority) {
            throw new AccessDeniedException("You have no rigths to access this information");
        }   
    }
}
