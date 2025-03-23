package com.mtrifonov.springsecuritystandard.validators;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.mtrifonov.springsecuritystandard.Role;
import static com.mtrifonov.springsecuritystandard.Role.*;

@Service
public class RoleChangeValidator {

    public static void validate(UserDetails admin, List<Role> currentRoles, List<Role> rolesForAdd, List<Role> rolesForDelete) {

        if (rolesForAdd.size() != 0 && rolesForDelete.size() != 0) { //covered
            throw new RuntimeException("You cannot grant and delete roles at the same time");
        }

        if (rolesForAdd.size() != 0) {

            var adminRole = greatestRole(admin.getAuthorities());
            var userRole = greatestRole(rolesForAdd);

            if (!(userRole.majority < adminRole.majority)) { //covered
                throw new RuntimeException(adminRole + " cannot grant " + userRole + " rights"); 
            }

            rolesForAdd.sort(ROLE_COMPARATOR);

            for (var role : rolesForAdd) { //covered
                if (role == ROLE_ADMIN && !currentRoles.contains(ROLE_USER)) {
                    throw new RuntimeException("It's impossible to grant " + ROLE_ADMIN + " without " + ROLE_USER);
                } else if (role == ROLE_SUPERADMIN && !currentRoles.contains(ROLE_ADMIN)) {
                    throw new RuntimeException("It's impossible to grant " + ROLE_SUPERADMIN + " without " + ROLE_ADMIN);
                } else if (role == ROLE_MASTER && !currentRoles.contains(ROLE_SUPERADMIN)) {
                    throw new RuntimeException("It's impossible to grant " + ROLE_MASTER + " without " + ROLE_SUPERADMIN);
                }

                currentRoles.add(role);
            }
        } else {

            var adminRole = greatestRole(admin.getAuthorities());
            var userRole = greatestRole(rolesForDelete);

            if (!(userRole.majority < adminRole.majority)) { //covered
                throw new RuntimeException(adminRole + " cannot delete " + userRole + " rights"); 
            }

            rolesForDelete.sort(ROLE_COMPARATOR);
            rolesForDelete = rolesForDelete.reversed();

            for (var role : rolesForDelete) {
                if (role == ROLE_SUPERADMIN && currentRoles.contains(ROLE_MASTER)) {
                    throw new RuntimeException("It's impossible to delete " + ROLE_SUPERADMIN + " with " + ROLE_MASTER);
                } else if (role == ROLE_ADMIN && currentRoles.contains(ROLE_SUPERADMIN)) {
                    throw new RuntimeException("It's impossible to delete " + ROLE_ADMIN + " with " + ROLE_SUPERADMIN);
                } else if (role == ROLE_USER && currentRoles.contains(ROLE_ADMIN)) {
                    throw new RuntimeException("It's impossible to delete " + ROLE_USER + " with " + ROLE_ADMIN);
                }

                currentRoles.remove(role);
            }
        }
    }
}
