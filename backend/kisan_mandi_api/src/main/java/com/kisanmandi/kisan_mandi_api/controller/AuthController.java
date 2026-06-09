package com.kisanmandi.kisan_mandi_api.controller;

import com.kisanmandi.kisan_mandi_api.dto.AuthResponse;
import com.kisanmandi.kisan_mandi_api.dto.LoginRequest;
import com.kisanmandi.kisan_mandi_api.dto.RegisterRequest;
import com.kisanmandi.kisan_mandi_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // ✅ FIX: always return JSON, never plain text
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // ✅ FIX: always return JSON, never plain text
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}