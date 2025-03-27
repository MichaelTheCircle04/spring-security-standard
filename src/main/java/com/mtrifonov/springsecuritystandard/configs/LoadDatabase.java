package com.mtrifonov.springsecuritystandard.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtrifonov.springsecuritystandard.UserDTO;
import com.mtrifonov.springsecuritystandard.services.RegistrationService;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

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
            var src = new ClassPathResource("users.json");
            var users = mapper.readValue(src.getFile(), new TypeReference<List<UserDTO>>(){});
            service.register(users.toArray(UserDTO[]::new));            
        };
    }

    @Bean
    @Profile("test")
    public DataSourceInitializer dataSourceInitializer(DataSource ds) {
        var popultator = new ResourceDatabasePopulator();
        popultator.addScript(new ClassPathResource("schema.sql"));
        popultator.execute(ds);
        var initializer = new DataSourceInitializer();
        initializer.setDataSource(ds);
        initializer.setDatabasePopulator(popultator);
        return initializer;
    }  
}
