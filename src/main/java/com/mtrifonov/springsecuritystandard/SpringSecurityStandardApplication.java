package com.mtrifonov.springsecuritystandard;

import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @Mikhail Trifonov
 */
@SpringBootApplication
public class SpringSecurityStandardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityStandardApplication.class, args);
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
