/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mtrifonov.springsecuritystandard;

/**
 *
 * @author 1
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
