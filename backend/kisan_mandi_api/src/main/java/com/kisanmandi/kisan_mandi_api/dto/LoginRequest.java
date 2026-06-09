package com.kisanmandi.kisan_mandi_api.dto;

import lombok.Data;

// This is what the frontend sends when logging in
@Data
public class LoginRequest {
    private String phone;
    private String password;
    private String role;
}