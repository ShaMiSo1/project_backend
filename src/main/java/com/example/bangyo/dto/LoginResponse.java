package com.example.bangyo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;      // JWT
    private String tokenType;  // "Bearer"
}
