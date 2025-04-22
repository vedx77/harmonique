package com.harmonique.userservice.payload;

import lombok.Data;

@Data
public class JwtAuthRequest {

    private String email;
    private String password;
}