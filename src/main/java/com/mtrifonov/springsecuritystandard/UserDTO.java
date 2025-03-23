package com.mtrifonov.springsecuritystandard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @Mikhail Trifonov
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    private Integer id;
    private String username;
    private String password;
    private String[] roles;
}
