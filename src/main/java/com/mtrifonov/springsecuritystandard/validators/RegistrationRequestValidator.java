package com.mtrifonov.springsecuritystandard.validators;

import com.mtrifonov.springsecuritystandard.UserDTO;
import com.mtrifonov.springsecuritystandard.exceptions.RegistrationException;
import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import lombok.AllArgsConstructor;
import java.sql.SQLException;
import org.springframework.stereotype.Service;

/**
 *
 * @Mikhail Trifonov
 */
@Service
@AllArgsConstructor
public class RegistrationRequestValidator {
    
    private final UserRepository userRepo;
    
    public void validate(UserDTO user) throws SQLException {
        if (userRepo.findUserByUsername(user.getUsername()).isPresent()) {
            throw new RegistrationException("User with name " + user.getUsername() + " already exist");
        } 
    }
}
