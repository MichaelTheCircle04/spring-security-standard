package com.mtrifonov.springsecuritystandard.controllers;

import com.mtrifonov.springsecuritystandard.services.RoleChangeService;
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
public class AdminController {
    
    private final DataCollector collector;
    private final RoleChangeService roleChangeService;

    public AdminController(DataCollector collector, RoleChangeService roleChangeService) {
        this.collector = collector;
        this.roleChangeService = roleChangeService;
    }
    
    public String produceAdminPage() {
        return "admin";
    }
    
    @GetMapping("/{id}")
    public String produceUserRolesPage(@AuthenticationPrincipal UserDetails admin, 
                                       @PathVariable int id, Model model) {
        admin
            .getAuthorities()
            .stream()
            .forEach(a -> System.out.println(a.getAuthority()));
        
        collector.collect(id, model, admin);
        return "user-roles-page";
    }
    
    @PostMapping("/change/role/{id}")
    public ResponseEntity<Void> changeRole(@RequestBody Roles roles, 
                                           @PathVariable int id) {
        
        roleChangeService.changeRoles(roles.rolesForAdd, roles.rolesForDelete, id);
        return ResponseEntity.ok().build();
    }
    
    public record Roles(String[] rolesForAdd, String[] rolesForDelete){}
}
