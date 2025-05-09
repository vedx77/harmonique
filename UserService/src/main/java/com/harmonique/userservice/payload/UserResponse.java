package com.harmonique.userservice.payload;

import com.harmonique.userservice.entity.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    
    private String profilePictureUrl;

    private String name;

    private String email;

    private String about;

    private Set<Role> roles;  // Role names like ["ROLE_ADMIN", "ROLE_USER"]
}
