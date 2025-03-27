package com.mtrifonov.springsecuritystandard.controllers;

import com.mtrifonov.springsecuritystandard.Role;
import com.mtrifonov.springsecuritystandard.services.DataCollector;
import com.mtrifonov.springsecuritystandard.services.RoleChangeService;
import com.mtrifonov.springsecuritystandard.validators.RoleChangeValidator;
import lombok.AllArgsConstructor;
import java.sql.SQLException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @Mikhail Trifonov
 */
@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final DataCollector collector;
    private final RoleChangeService roleChangeService;
    
    @GetMapping
    public String produceAdminPage() { 
        return "admin";
    }
    
    @GetMapping("/{userId}")
    public String produceUserRolesPage(
        @PathVariable Integer userId, Model model) throws SQLException { 
        
        collector.collect(userId, model);
        return "user-roles";
    }
    
    @PostMapping("/change/role/{id}")
    public ResponseEntity<Void> changeRole(
        @AuthenticationPrincipal UserDetails admin,
        @RequestBody Roles roles, @PathVariable Integer id) { 
        
        RoleChangeValidator.validate(admin, roles.currentRoles, roles.rolesForAdd, roles.rolesForDelete);
        roleChangeService.changeRoles(roles.rolesForAdd, roles.rolesForDelete, id);
        return ResponseEntity.ok().build();
    }
    
    public record Roles(List<Role> currentRoles, List<Role> rolesForAdd, List<Role> rolesForDelete){}
}
