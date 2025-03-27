package com.mtrifonov.springsecuritystandard;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
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
    @WithMockUser
    void getUserPage_withUserRights() throws Exception {
        mvc.perform(get("/user")).andExpect(status().isOk());
    }

    @Test
    void getUserPage_anonymous() throws Exception {
        mvc.perform(get("/user"))
            .andExpectAll(
                status().isFound(),
                redirectedUrl("http://localhost/login")
            );
    }

    @Test
    @WithMockUser(username = "i.ivanov", roles = {"USER"})
    void getUserById_self() throws Exception {
        mvc.perform(get("/users/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "u.rucus", roles = {"USER", "ADMIN"}) 
    void getUserById_withAdminRights() throws Exception {
        mvc.perform(get("/users/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "i.ivanov", roles = {"USER"})
    void getUserById_withUserRights() throws Exception {
        mvc.perform(get("/users/2"))
            .andExpectAll(
                status().isForbidden(), 
                forwardedUrl("/access-denied")
            );
    }

    @Test
    void getLoginPage_anonymous() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getLoginPage_authorized() throws Exception {
        mvc.perform(get("/login"))
            .andExpectAll(
                status().isForbidden(), 
                forwardedUrl("/access-denied")
            );
    }

    @Test
    void getRegistrationPage_anonymous() throws Exception {
        mvc.perform(get("/registration")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getRegistrationPage_authorized() throws Exception {
        mvc.perform(get("/registration"))
            .andExpectAll(
                status().isForbidden(), 
                forwardedUrl("/access-denied")
            );
    }

    @Test
    @DirtiesContext
    void registerUser_anonymous_validRequest() throws Exception {

        var content =
                    """
                    {
                        "username": "p.person",
                        "password": "password",
                        "roles": ["ROLE_USER"]
                    }
                    """;
        
        var response = mvc.perform(
            post("/registration")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            )
                .andExpect(
                    status().isCreated()
                    ).andReturn().getResponse(); 

        mvc.perform(
            get(response.getHeader("location"))
                .with(user("p.person")
                .password("password")
                .roles("USER")
                )).andExpectAll(
                    status().isOk(), 
                    content().string(containsString("p.person"))
                    ).andReturn();
    }

    @Test
    @WithMockUser
    void registerUser_authorized_validRequest() throws Exception {

        var content =
                    """
                    {
                        "username": "p.person",
                        "password": "password",
                        "roles": ["ROLE_USER"]
                    }
                    """;

        mvc.perform(
            post("/registration")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            )
                .andExpectAll(
                    status().isForbidden(), 
                    forwardedUrl("/access-denied")
                );
    }

    @Test
    void registerUser_anonymous_invalidBody() throws Exception {

        var builder = post("/registration").contentType(MediaType.APPLICATION_JSON).with(csrf());

        var content =
                    """
                    {
                        "password": "password",
                        "roles": ["ROLE_USER"]
                    }
                    """;

        mvc.perform(builder.content(content)).andExpect(status().isBadRequest());

        content =
                    """
                    {
                        "username": "p.person",
                        "roles": ["ROLE_USER"]
                    }
                    """;

        mvc.perform(builder.content(content)).andExpect(status().isBadRequest());

        content =
                """
                {
                    "username": "p.person",
                    "password": "password"
                }
                """;

        mvc.perform(builder.content(content)).andExpect(status().isBadRequest());
    }
}
