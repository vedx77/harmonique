package com.harmonique.userservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class JwtAuthRequest {

    private String email;
    private String password;
}
