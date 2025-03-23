package com.mtrifonov.springsecuritystandard.repositories;

import java.util.Collections;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.mtrifonov.springsecuritystandard.Role;
import lombok.AllArgsConstructor;

/**
 *
 * @Mikhail Trifonov
 */
@Repository
@AllArgsConstructor
public class RoleRepository {
    
    public final JdbcTemplate jdbcTemplate;
    
    public List<Role> getRolesById(int id) {
        
        String sql = """
                     SELECT authority
                     FROM authorities 
                     JOIN users USING (username) 
                     WHERE id = ?
                     """;
        try {
            return jdbcTemplate.queryForList(sql, Role.class, id);
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }
           
    public List<Role> getRolesByUsername(String username) {
        
        String sql = """
                     SELECT authority
                     FROM authorities
                     WHERE username = ?
                     """;
        try {
            return jdbcTemplate.queryForList(sql, Role.class, username);
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }
    
    public void addRolesById(List<Role> roles, int id) {
        
        String sql = """
                     INSERT INTO authorities (username, authority)
                     VALUES (SELECT username FROM users WHERE id = ?, ?)
                     """;
        
        for (var role : roles) {
            jdbcTemplate.update(sql, id, role.name());
        }
    }
    
    public void addRolesByUsername(List<Role> roles, String username) {
        
        String sql = """
                     INSERT INTO authorities (username, authority)
                     VALUES (?, ?)
                     """;
        
        for (var role : roles) {
            jdbcTemplate.update(sql, username, role.name());
        }
    }
    
    public void deleteRolesById(List<Role> roles, int id) {
        
        String sql = """
                     DELETE FROM authorities
                     WHERE 
                        username IN (SELECT username FROM users WHERE id = ?) 
                        AND authority = ?
                     """;
        
        for (var role : roles) {
            jdbcTemplate.update(sql, id, role.name());
        }
    }
    
    public void deleteRolesByUsername(List<Role> roles, String username) {
        
        String sql = """
                     DELETE FROM authorities
                     WHERE username = ? AND authority = ?
                     """;
                
        for (var role : roles) {
            jdbcTemplate.update(sql, username, role.name());
        }
    }
}
