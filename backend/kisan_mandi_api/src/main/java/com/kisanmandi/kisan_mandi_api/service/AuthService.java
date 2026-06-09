package com.kisanmandi.kisan_mandi_api.service;

import com.kisanmandi.kisan_mandi_api.config.JwtUtil;
import com.kisanmandi.kisan_mandi_api.dto.AuthResponse;
import com.kisanmandi.kisan_mandi_api.dto.LoginRequest;
import com.kisanmandi.kisan_mandi_api.dto.RegisterRequest;
import com.kisanmandi.kisan_mandi_api.model.User;
import com.kisanmandi.kisan_mandi_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ============================================
    // REGISTER
    // ============================================
    public AuthResponse register(RegisterRequest request) {
        // Clean phone number — remove +91 and spaces
        String cleanPhone = request.getPhone()
                .replace("+91", "")
                .replace(" ", "")
                .trim();
        request.setPhone(cleanPhone);

        // Check if phone already exists
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already registered!");
        }

        // Create new user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setVillage(request.getVillage());
        user.setDistrict(request.getDistrict());
        user.setState(request.getState());
        user.setStatus("PENDING");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save to MongoDB
        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(
                savedUser.getPhone(),
                savedUser.getRole(),
                savedUser.getId()
        );

        // Return token + user info
        return new AuthResponse(
                token,
                savedUser.getRole(),
                savedUser.getFirstName(),
                savedUser.getId(),
                savedUser.getState()
        );
    }

    // ============================================
    // LOGIN
    // ============================================
    public AuthResponse login(LoginRequest request) {
        // Clean phone number — remove +91 and spaces
        String cleanPhone = request.getPhone()
                .replace("+91", "")
                .replace(" ", "")
                .trim();
        request.setPhone(cleanPhone);

        // Find user by phone
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("Phone number not registered!"));

        // ✅ FIX 1: Validate that the selected role matches the account role
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            if (!user.getRole().equalsIgnoreCase(request.getRole())) {
                throw new RuntimeException(
                        "This phone is registered as " + user.getRole() +
                                ", not " + request.getRole() + ". Please select the correct role."
                );
            }
        }

        // Check if account is blocked
        if (user.getStatus().equals("BLOCKED")) {
            throw new RuntimeException("Your account has been blocked!");
        }

        // ✅ FIX 2: Check password with BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password!");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(
                user.getPhone(),
                user.getRole(),
                user.getId()
        );

        // Return token + user info
        return new AuthResponse(
                token,
                user.getRole(),
                user.getFirstName(),
                user.getId(),
                user.getState()
        );
    }
}