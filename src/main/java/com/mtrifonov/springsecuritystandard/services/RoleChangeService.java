package com.mtrifonov.springsecuritystandard.services;

import com.mtrifonov.springsecuritystandard.Role;
import com.mtrifonov.springsecuritystandard.repositories.RoleRepository;

import lombok.AllArgsConstructor;

import java.util.List;


import org.springframework.stereotype.Service;

/**
 *
 * @Mikhail Trifonov
 */
@Service
@AllArgsConstructor
public class RoleChangeService {
    
    private final RoleRepository roleRepo;
    
    public void changeRoles(List<Role> rolesForAdd, List<Role> rolesForDelete, int id) {
        
        if (rolesForAdd.size() != 0) {
            addRoles(rolesForAdd, id);
        } else {
            deleteRoles(rolesForDelete, id);
        }
    }
    
    private void addRoles(List<Role> roles, int id) {
        roleRepo.addRolesById(roles, id);
    }
    
    private void deleteRoles(List<Role> roles, int id) {
        roleRepo.deleteRolesById(roles, id);
    }
}
