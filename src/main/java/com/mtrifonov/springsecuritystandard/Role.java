package com.mtrifonov.springsecuritystandard;

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
}
