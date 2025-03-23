package com.mtrifonov.springsecuritystandard;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mtrifonov.springsecuritystandard.configs.LoadDatabase;
import com.mtrifonov.springsecuritystandard.configs.SecurityConfig;
import com.mtrifonov.springsecuritystandard.controllers.MainController;
import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import com.mtrifonov.springsecuritystandard.services.RegistrationService;
import com.mtrifonov.springsecuritystandard.validators.RegistrationRequestValidator;

@WebMvcTest(controllers = MainController.class)
@ContextConfiguration(classes = 
    {
        LoadDatabase.class, SecurityConfig.class, 
        UserRepository.class, RegistrationService.class, 
        RegistrationRequestValidator.class, 
        SpringSecurityStandardApplication.class
    })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
public class MainControllerTest {

    MockMvc mvc;

    @BeforeEach
    void setupMvc(WebApplicationContext ctx) {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
    }

    @Test
    void getMainPage_anonymous() throws Exception {
        mvc.perform(get("/main")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getUserPage_withUserRights() throws Exception {
        mvc.perform(get("/user")).andExpect(status().isOk());
    }
}
