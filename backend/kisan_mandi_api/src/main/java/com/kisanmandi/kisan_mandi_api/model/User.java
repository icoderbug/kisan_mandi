package com.kisanmandi.kisan_mandi_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

// This tells Spring Boot this class maps to "users" collection in MongoDB
@Document(collection = "users")
@Data  // Lombok — auto generates getters, setters, toString
public class User {

    @Id
    private String id;  // MongoDB auto generates this

    private String firstName;
    private String lastName;

    @Indexed(unique = true)  // no two users can have same phone
    private String phone;

    private String email;
    private String password;  // will be encrypted with BCrypt

    // Role — FARMER, BUYER, or ADMIN
    private String role;

    // Location details
    private String village;
    private String district;
    private String state;

    // Account status
    private String status;  // PENDING, VERIFIED, BLOCKED

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}