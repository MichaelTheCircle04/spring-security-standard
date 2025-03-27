package com.mtrifonov.springsecuritystandard;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @Mikhail Trifonov
 */
public enum Role {
    ROLE_USER(1),
    ROLE_ADMIN(2),
    ROLE_SUPERADMIN(3),
    ROLE_MASTER(4);
    
    public int majority;
    
    Role(int majority) {
        this.majority = majority;
    }

    public static final Comparator<Role> ROLE_COMPARATOR = (r1, r2) -> {

        if (r1.majority > r2.majority) {
            return 1;
        } else if (r1.majority == r2.majority) {
            return 0;
        } else {
            return -1;
        }
    };

    public static Role greatestRole(Collection<? extends GrantedAuthority> roles) {

        var greatestRole = Role.ROLE_USER;

        for (var role : roles) {

            var cur = Role.valueOf(role.getAuthority());
            if (cur.majority > greatestRole.majority) {
                greatestRole = cur;
            }
        }

        return greatestRole;
    }

    public static Role greatestRole(List<Role> roles) {

        var greatestRole = Role.ROLE_USER;

        for (var role : roles) {

            if (role.majority > greatestRole.majority) {
                greatestRole = role;
            }
        }

        return greatestRole;
    }
}
