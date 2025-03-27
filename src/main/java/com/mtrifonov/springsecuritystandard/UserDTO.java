package com.mtrifonov.springsecuritystandard;

import java.util.List;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private List<Role> roles;
}
