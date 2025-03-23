package com.mtrifonov.springsecuritystandard.services;

import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import lombok.AllArgsConstructor;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 *
 * @Mikhail Trifonov
 */
@Component
@AllArgsConstructor
public class DataCollector {

    private final UserRepository repo;
    
    public void collect(Integer userId, Model model) throws SQLException {
        
        var user = 
            repo.findUserById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id: " + userId + " does not exist"));

        model.addAttribute("user", user);
    }
}
