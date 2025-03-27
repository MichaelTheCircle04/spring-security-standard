package com.mtrifonov.springsecuritystandard;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.mtrifonov.springsecuritystandard.configs.LoadDatabase;
import com.mtrifonov.springsecuritystandard.configs.SecurityConfig;
import com.mtrifonov.springsecuritystandard.controllers.AccessDeniedController;
import com.mtrifonov.springsecuritystandard.controllers.AdminController;
import com.mtrifonov.springsecuritystandard.repositories.RoleRepository;
import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import com.mtrifonov.springsecuritystandard.services.DataCollector;
import com.mtrifonov.springsecuritystandard.services.RegistrationService;
import com.mtrifonov.springsecuritystandard.services.RoleChangeService;

@WebMvcTest(controllers = {AdminController.class, AccessDeniedController.class})
@ContextConfiguration(classes = 
    {
        LoadDatabase.class, SecurityConfig.class, 
        RoleRepository.class, UserRepository.class, 
        DataCollector.class, RoleChangeService.class,
        RegistrationService.class, SpringSecurityStandardApplication.class
    })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
public class AdminControllerTest {

    MockMvc mvc;

    @BeforeEach
    void setupMockMvc(WebApplicationContext ctx) {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getAdminPage_withAdminRights() throws Exception {

        mvc.perform(get("/admin"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAdminPage_withoutAdminRights() throws Exception {

        mvc.perform(get("/admin"))
            .andExpectAll(status().isForbidden(), forwardedUrl("/access-denied"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getUserRolesPage_withAdminRights_validReuqest() throws Exception {

        mvc.perform(get("/admin/3")).
            andExpectAll(
                status().isOk(),
                content().string(containsString("ROLE_SUPERADMIN")),
                content().string(containsString("ROLE_ADMIN")),
                content().string(containsString("ROLE_USER"))
                );
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUserRolesPage_withoutAdminRights_validReuqest() throws Exception {

        mvc.perform(get("/admin/3")).
            andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getUserRolesPage_withAdminRights_invalidId() throws Exception {

        mvc.perform(get("/admin/10"))
            .andExpectAll(
                status().isBadRequest(),
                content().string(containsString("User with id: 10 does not exist"))
                );
    }

    @Test
    @WithMockUser(username = "master", roles = {"USER", "ADMIN", "SUPERADMIN", "MASTER"})
    @DirtiesContext
    void changeRoles_withMasterRights_validRequest() throws Exception {

        var content = 
                    """
                    {
                        "currentRoles": ["ROLE_USER"],
                        "rolesForAdd": ["ROLE_ADMIN", "ROLE_SUPERADMIN"],
                        "rolesForDelete": []
                    }
                    """;

        mvc.perform(
                post("/admin/change/role/1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
            ).andExpect(status().isOk());
        

        mvc.perform(get("/admin/1")).
            andExpectAll(
                status().isOk(),
                content().string(containsString("<td>ROLE_SUPERADMIN</td>")),
                content().string(containsString("<td>ROLE_ADMIN</td>")),
                content().string(containsString("<td>ROLE_USER</td>"))
                );
    }

    @Test
    @WithMockUser(username = "master", roles = {"USER", "ADMIN", "SUPERADMIN", "MASTER"})
    @DirtiesContext
    void deleteRoles_withMasterRights_validRequest() throws Exception {

        var content = 
                    """
                    {
                        "currentRoles": ["ROLE_USER", "ROLE_ADMIN", "ROLE_SUPERADMIN"],
                        "rolesForAdd": [],
                        "rolesForDelete": ["ROLE_ADMIN", "ROLE_SUPERADMIN"]
                    }
                    """;

        mvc.perform(
                post("/admin/change/role/3")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
            ).andExpect(status().isOk());
        

        var body = mvc.perform(get("/admin/1")).
            andExpectAll(
                status().isOk(),
                content().string(containsString("<td>ROLE_USER</td>"))
                ).andReturn().getResponse().getContentAsString();

        assertFalse(body.contains("<td>ROLE_ADMIN</td>") || body.contains("<td>ROLE_SUPERADMIN</td>"));
    }

    @Test
    @WithMockUser(username = "superadmin", roles = {"USER", "ADMIN", "SUPERADMIN"})
    void changeRole_withSuperadminRights_invalidAuthority() throws Exception {

        var content = 
                    """
                    {
                        "currentRoles": ["ROLE_USER"],
                        "rolesForAdd": ["ROLE_ADMIN", "ROLE_SUPERADMIN"],
                        "rolesForDelete": []
                    }
                    """;

        mvc.perform(
                post("/admin/change/role/1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
            ).andExpectAll(
                status().isBadRequest(),
                content().string(containsString("ROLE_SUPERADMIN cannot grant ROLE_SUPERADMIN rights"))
                );
    }

    @Test
    @WithMockUser(username = "superadmin", roles = {"USER", "ADMIN", "SUPERADMIN"})
    void deleteRole_withSuperadminRights_invalidAuthority() throws Exception {

        var content = 
                    """
                    {
                        "currentRoles": ["ROLE_USER", "ROLE_ADMIN", "ROLE_SUPERADMIN"],
                        "rolesForAdd": [],
                        "rolesForDelete": ["ROLE_SUPERADMIN"]
                    }
                    """;

        mvc.perform(
                post("/admin/change/role/3")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
            ).andExpectAll(
                status().isBadRequest(),
                content().string(containsString("ROLE_SUPERADMIN cannot delete ROLE_SUPERADMIN rights"))
                );
    }

    @Test
    @WithMockUser(username = "master", roles = {"USER", "ADMIN", "SUPERADMIN", "MASTER"})
    void changeRole_withSuperadminRights_invalidBody() throws Exception {

        var builder = post("/admin/change/role/1").contentType(MediaType.APPLICATION_JSON).with(csrf());

        var content = 
                    """
                    {
                        "currentRoles": ["ROLE_USER", "ROLE_ADMIN"],
                        "rolesForAdd": ["ROLE_SUPERADMIN"],
                        "rolesForDelete": ["ROLE_ADMIN"]
                    }
                    """; 
        
        mvc.perform(builder.content(content))
            .andExpectAll(
                status().isBadRequest(), 
                content().string(containsString("You cannot grant and delete roles at the same time"))
                );

        content = 
                    """
                    {
                        "currentRoles": ["ROLE_USER"],
                        "rolesForAdd": ["ROLE_SUPERADMIN"],
                        "rolesForDelete": []
                    }
                    """;
        
        mvc.perform(builder.content(content))
            .andExpectAll(
                status().isBadRequest(), 
                content().string(containsString("It's impossible to grant ROLE_SUPERADMIN without ROLE_ADMIN"))
                );

        content = 
                    """
                    {
                        "currentRoles": ["ROLE_USER", "ROLE_ADMIN", "ROLE_SUPERADMIN"],
                        "rolesForAdd": [],
                        "rolesForDelete": ["ROLE_ADMIN"]
                    }
                    """;
        
        mvc.perform(builder.content(content))
            .andExpectAll(
                status().isBadRequest(), 
                content().string(containsString("It's impossible to delete ROLE_ADMIN with ROLE_SUPERADMIN"))
                );
    }
}
