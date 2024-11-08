package com.mtrifonov.springsecuritystandard.validators;

import com.mtrifonov.springsecuritystandard.UserRepresentation;
import com.mtrifonov.springsecuritystandard.controllers.RegistrationException;
import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @Mikhail Trifonov
 */
@Service
public class RegistrationRequestValidator {
    
    
    private final UserRepository userRepo;
    
    @Autowired
    public RegistrationRequestValidator(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    public void validate(UserRepresentation user) {
        if (userRepo.findUserByUsername(user.getUsername()) != null) {
            throw new RegistrationException("User with name " + user.getUsername() + " already exist");
        } 
    }
}
