package com.mtrifonov.springsecuritystandard.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtrifonov.springsecuritystandard.UserRepresentation;
import com.mtrifonov.springsecuritystandard.services.RegistrationService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @Mikhail Trifonov
 */
@Configuration
public class LoadDatabase {
    
    @Value("${users}")
    private String path;
    
    private final ObjectMapper mapper = new ObjectMapper();
    private final RegistrationService service;

    public LoadDatabase(RegistrationService service) {
        this.service = service;
    }
    
    @Bean
    public CommandLineRunner initDatabase() throws IOException {
        
        return args -> {
            File src = new File(path);
            List<UserRepresentation> users = mapper.readValue(src, new TypeReference<List<UserRepresentation>>(){});
            service.register(users.toArray());            
        };
    }    
}
