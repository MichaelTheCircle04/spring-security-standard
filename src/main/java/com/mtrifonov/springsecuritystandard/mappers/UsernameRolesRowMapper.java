package com.mtrifonov.springsecuritystandard.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @Mikhail Trifonov
 */
@Component
public class UsernameRolesRowMapper implements RowMapper {

    @Override
    public List<Map<String, Object>> mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if (rs.isBeforeFirst()) {
            rs.next();
        }
        while (true) {
            convertRowToMap(rs, map, result);
            if (!rs.next()) {
                break;
            }
        }
        return result;
    }
    
    
    private void convertRowToMap(ResultSet rs, Map<String, Object> map, List result) throws SQLException {
        String username = rs.getString("username");
        if (!map.isEmpty() && map.get("username").equals(username)) {
            var roles = (List<String>) map.get("roles");
            roles.add(rs.getString("authority"));
            if (!result.contains(map)) {
                result.add(map);
            }
            return;
        } else if (!map.isEmpty()) {
            map = new HashMap<>();
        }
        map.put("username", username);
        var roles = new ArrayList<String>();
        roles.add(rs.getString("authority"));
        map.put("roles", roles);
        result.add(map);
    }
}
