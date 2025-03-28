package com.mtrifonov.springsecuritystandard.mappers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.mtrifonov.springsecuritystandard.Role;
import com.mtrifonov.springsecuritystandard.UserDTO;

/**
 *
 * @Mikhail Trifonov
 */
public class UserRowMapper {

    public static List<UserDTO> exctractList(SqlRowSet rs) throws SQLException {


        var result = new ArrayList<UserDTO>();
        boolean last = !rs.first();

        while (!last) {
            last = convertRowToUserRepresentation(rs, result);
        }

        return result;
    }
    
    private static boolean convertRowToUserRepresentation(SqlRowSet rs, List<UserDTO> result) throws SQLException {

        var username = rs.getString("username");
        
        var builder = UserDTO
            .builder()
            .id(rs.getInt("id"))
            .username(username)
            .password(rs.getString("password"));

        var roles = rs.getString("authority");

        boolean last = true;
        while (rs.next()) {

            if (rs.getString("username") == username) {
                roles += ", " + rs.getString("authority");
            } else {
                last = false;
            }
        }

        var rolesList = Stream.of(roles.split(", ")).map(r -> Role.valueOf(r)).toList();
        result.add(builder.roles(rolesList).build());
        return last;
    }
}
