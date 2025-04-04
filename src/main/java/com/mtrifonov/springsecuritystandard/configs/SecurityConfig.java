package com.mtrifonov.springsecuritystandard.configs;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @Mikhail Trifonov
 */
@Configuration
public class SecurityConfig {
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {       
        http.formLogin(a -> a.loginPage("/login"));
        http.logout(l -> l.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl("/login"));
        http.exceptionHandling(e -> e.accessDeniedPage("/access-denied"));
        http.authorizeHttpRequests(a -> a.requestMatchers("/user", "/users/**").authenticated()
                .requestMatchers("/login", "/registration").anonymous()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());        
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsManager(DataSource ds) {        
        return new JdbcUserDetailsManager(ds);        
    }
    
    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }    
}
