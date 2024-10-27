package com.mtrifonov.springsecuritystandard.controllers;

import com.mtrifonov.springsecuritystandard.Role;
import com.mtrifonov.springsecuritystandard.repositories.UserRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 *
 * @Mikhail Trifonov
 */
@Component
public class DataCollector {
    
    private final UserRepository userRepo;
    
    @Autowired
    public DataCollector(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    public void collect(int id, Model model, UserDetails admin) {
        model.addAttribute("adminAuthority", greatestRole(admin.getAuthorities()));
        model.addAllAttributes(userRepo.findUserById(id));
    }
    
    private String greatestRole(Collection<? extends GrantedAuthority> roles) {
        Role greatestRole = Role.ROLE_USER;
        for (GrantedAuthority role : roles) {
            Role cur = Role.valueOf(role.getAuthority());
            if (cur.majority > greatestRole.majority) {
                greatestRole = cur;
            }
        }
        return greatestRole.name();
    }
}
