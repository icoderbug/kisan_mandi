package com.kisanmandi.kisan_mandi_api.dto;

import lombok.Data;

// This is what the frontend sends when registering
@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String role;        // FARMER, BUYER, ADMIN
    private String village;
    private String district;
    private String state;
}