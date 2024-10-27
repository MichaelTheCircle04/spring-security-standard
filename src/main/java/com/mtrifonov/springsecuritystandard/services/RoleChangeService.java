package com.mtrifonov.springsecuritystandard.services;

import com.mtrifonov.springsecuritystandard.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @Mikhail Trifonov
 */
@Service
public class RoleChangeService {
    
    private final RoleRepository roleRepo;

    @Autowired
    public RoleChangeService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }
    
    private void addRoles(String[] roles, int id) {
        roleRepo.addRolesById(roles, id);
    }
    
    private void deleteRoles(String[] roles, int id) {
        roleRepo.deleteRolesById(roles, id);
    }

    public void changeRoles(String[] rolesForAdd, String[] rolesForDelete, int id) {
        addRoles(rolesForAdd, id);
        deleteRoles(rolesForDelete, id);
    }
    
}
