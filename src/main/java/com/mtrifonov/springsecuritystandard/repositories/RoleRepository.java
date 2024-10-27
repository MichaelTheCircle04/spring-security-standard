package com.mtrifonov.springsecuritystandard.repositories;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @Mikhail Trifonov
 */
@Repository
public class RoleRepository {
    
    public final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<String> getRolesById(int id) {
        
        String sql = """
                     SELECT authority
                     FROM authorities 
                     JOIN users USING (username) 
                     WHERE id = ?
                     """;
        try {
            return jdbcTemplate.queryForList(sql, String.class, id);
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }
           
    public List<String> getRolesByUsername(String username) {
        
        String sql = """
                     SELECT authority
                     FROM authorities
                     WHERE username = ?
                     """;
        try {
            return jdbcTemplate.queryForList(sql, String.class, username);
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }
    
    public void addRolesById(String[] roles, int id) {
        
        String sql = """
                     INSERT INTO authorities (username, authority)
                     VALUES (SELECT username FROM users WHERE id = ?, ?)
                     """;
        
        for (String role : roles) {
            jdbcTemplate.update(sql, id, role);
        }
    }
    
    public void addRolesByUsername(String[] roles, String username) {
        
        String sql = """
                     INSERT INTO authorities (username, authority)
                     VALUES (?, ?)
                     """;
        
        for (String role : roles) {
            jdbcTemplate.update(sql, username, role);
        }
    }
    
    public void deleteRolesById(String[] roles, int id) {
        
        String sql = """
                     DELETE FROM authorities
                     WHERE username = ? AND authority = ?
                     """;
        
        String username = jdbcTemplate.queryForObject("SELECT username FROM users WHERE id = ?", String.class, id);
        for (String role : roles) {
            jdbcTemplate.update(sql, username, role);
        }
    }
    
    public void deleteRolesByUsername(String[] roles, String username) {
        
        String sql = """
                     DELETE FROM authorities
                     WHERE username = ? AND authority = ?
                     """;
                
        for (String role : roles) {
            jdbcTemplate.update(sql, username, role);
        }
    }
}
