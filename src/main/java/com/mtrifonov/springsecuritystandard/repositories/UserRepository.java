package com.mtrifonov.springsecuritystandard.repositories;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @Mikhail Trifonov
 */
@Repository
public class UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper uprm;
    
    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, RowMapper uprm) {
        this.jdbcTemplate = jdbcTemplate;
        this.uprm = uprm;
    }
    
    public Map<String, Object> findUserById(int id) {
        String sql = """
                     SELECT *
                     FROM users 
                     JOIN authorities USING(username)  
                     WHERE id = ?
                     ORDER BY username
                     """;
        try {
            var res = (List<Map<String, Object>>) jdbcTemplate.queryForObject(sql, uprm, id);
            return res.get(0);
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    public Map<String, Object> findUserByUsername(String username) {
        String sql = """
                     SELECT *
                     FROM users
                     JOIN authorities USING(username)
                     WHERE username = ?
                     ORDER BY username
                     """;
        
        try {
            var res = (List<Map<String, Object>>) jdbcTemplate.queryForObject(sql, uprm, username);
            return res.get(0);
        } catch (DataAccessException e) {
            return null;
        }
    }
}
