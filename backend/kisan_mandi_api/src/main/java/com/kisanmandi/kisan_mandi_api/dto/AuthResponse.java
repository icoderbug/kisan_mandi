package com.kisanmandi.kisan_mandi_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

// This is what we send BACK to frontend after login/register
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;       // JWT token
    private String role;        // FARMER, BUYER, ADMIN
    private String name;        // user's first name
    private String userId;      // user's MongoDB id
    private String location;    // user's state
}