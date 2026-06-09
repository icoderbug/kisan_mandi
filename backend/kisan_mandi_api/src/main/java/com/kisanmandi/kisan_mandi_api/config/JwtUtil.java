package com.kisanmandi.kisan_mandi_api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// This class creates and verifies JWT tokens
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Get the signing key from secret
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Create a JWT token for a user
    public String generateToken(String phone, String role, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get phone number from token
    public String getPhoneFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Get role from token
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Get userId from token
    public String getUserIdFromToken(String token) {
        return getClaims(token).get("userId", String.class);
    }

    // Check if token is expired
    public Boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Check if token is valid
    public Boolean validateToken(String token) {
        try {
            getClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Read all claims from token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}