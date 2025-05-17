//package com.harmonique.userservice.payload;
//
//import com.harmonique.userservice.entity.Role;
//import lombok.*;
//
//import java.util.Set;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserResponse {
//
//    private Long id;
//    
//    private String profilePictureUrl;
//
//    private String name;
//
//    private String email;
//
//    private String about;
//
//    private Set<Role> roles;  // Role names like ["ROLE_ADMIN", "ROLE_USER"]
//}

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

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNo;

    private String about;

    private String location;

    private Set<Role> roles;  // Role names like ["ROLE_ADMIN", "ROLE_USER"]
}