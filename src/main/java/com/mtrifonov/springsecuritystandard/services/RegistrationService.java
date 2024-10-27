package com.mtrifonov.springsecuritystandard.services;

import com.mtrifonov.springsecuritystandard.UserRepresentation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 *
 * @Mikhail Trifonov
 */
@Service
public class RegistrationService {
    
    private final JdbcUserDetailsManager manager;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationService(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.manager = (JdbcUserDetailsManager) userDetailsService;
        this.encoder = encoder;
    }
    
    public void register(Object... args) {
        var users = List.of(args).stream().map(a -> (UserRepresentation) a).toList();
        
        for (var user : users) {
            var userDetails = User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .passwordEncoder(e -> encoder.encode(e))
                    .build();           
            manager.createUser(userDetails);
        }
    }
}
