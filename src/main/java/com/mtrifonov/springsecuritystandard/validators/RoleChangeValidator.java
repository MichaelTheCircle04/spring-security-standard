package com.mtrifonov.springsecuritystandard.validators;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import com.mtrifonov.springsecuritystandard.Role;
import com.mtrifonov.springsecuritystandard.exceptions.RoleChangeException;
import static com.mtrifonov.springsecuritystandard.Role.*;

/**
 *
 * @Mikhail Trifonov
 */
public class RoleChangeValidator {

    public static void validate(UserDetails admin, List<Role> currentRoles, List<Role> rolesForAdd, List<Role> rolesForDelete) {

        if (rolesForAdd.size() != 0 && rolesForDelete.size() != 0) {
            throw new RoleChangeException("You cannot grant and delete roles at the same time");
        }

        if (rolesForAdd.size() != 0) {

            var adminRole = greatestRole(admin.getAuthorities());
            var userRole = greatestRole(rolesForAdd);

            if (!(userRole.majority < adminRole.majority)) {
                throw new RoleChangeException(adminRole + " cannot grant " + userRole + " rights"); 
            }

            rolesForAdd.sort(ROLE_COMPARATOR);

            for (var role : rolesForAdd) { 
                if (role == ROLE_ADMIN && !currentRoles.contains(ROLE_USER)) {
                    throw new RoleChangeException("It's impossible to grant " + ROLE_ADMIN + " without " + ROLE_USER);
                } else if (role == ROLE_SUPERADMIN && !currentRoles.contains(ROLE_ADMIN)) {
                    throw new RoleChangeException("It's impossible to grant " + ROLE_SUPERADMIN + " without " + ROLE_ADMIN);
                } else if (role == ROLE_MASTER && !currentRoles.contains(ROLE_SUPERADMIN)) {
                    throw new RoleChangeException("It's impossible to grant " + ROLE_MASTER + " without " + ROLE_SUPERADMIN);
                }

                currentRoles.add(role);
            }
        } else {

            var adminRole = greatestRole(admin.getAuthorities());
            var userRole = greatestRole(rolesForDelete);

            if (!(userRole.majority < adminRole.majority)) { 
                throw new RoleChangeException(adminRole + " cannot delete " + userRole + " rights"); 
            }

            rolesForDelete.sort(ROLE_COMPARATOR);
            rolesForDelete = rolesForDelete.reversed();

            for (var role : rolesForDelete) {
                if (role == ROLE_SUPERADMIN && currentRoles.contains(ROLE_MASTER)) {
                    throw new RoleChangeException("It's impossible to delete " + ROLE_SUPERADMIN + " with " + ROLE_MASTER);
                } else if (role == ROLE_ADMIN && currentRoles.contains(ROLE_SUPERADMIN)) {
                    throw new RoleChangeException("It's impossible to delete " + ROLE_ADMIN + " with " + ROLE_SUPERADMIN);
                } else if (role == ROLE_USER && currentRoles.contains(ROLE_ADMIN)) {
                    throw new RoleChangeException("It's impossible to delete " + ROLE_USER + " with " + ROLE_ADMIN);
                }

                currentRoles.remove(role);
            }
        }
    }
}
