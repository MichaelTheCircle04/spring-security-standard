package com.mtrifonov.springsecuritystandard.repositories;

import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.mtrifonov.springsecuritystandard.UserDTO;
import com.mtrifonov.springsecuritystandard.mappers.UsernameRolesRowMapper;

import lombok.AllArgsConstructor;

/**
 *
 * @Mikhail Trifonov
 */
@Repository
@AllArgsConstructor
public class UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public Optional<UserDTO> findUserById(int id) throws SQLException {

        String sql = """
                     SELECT *
                     FROM users 
                     JOIN authorities USING(username)  
                     WHERE id = ?
                     ORDER BY username
                     """;
            
        var rs = jdbcTemplate.queryForRowSet(sql, id);
        var res = UsernameRolesRowMapper.exctractList(rs);
        if (res.isEmpty()) { 
            return Optional.empty();
        } else {
            return Optional.of(res.get(0));
        }
    }
    
    public Optional<UserDTO> findUserByUsername(String username) throws SQLException {

        String sql = """
                     SELECT *
                     FROM users
                     JOIN authorities USING(username)
                     WHERE username = ?
                     ORDER BY username
                     """;
        
        var rs = jdbcTemplate.queryForRowSet(sql, username);
        var res = UsernameRolesRowMapper.exctractList(rs);
        if (res.isEmpty()) { 
            return Optional.empty();
        } else {
            return Optional.of(res.get(0));
        }
    }
}
