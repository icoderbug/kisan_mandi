package com.kisanmandi.kisan_mandi_api.repository;

import com.kisanmandi.kisan_mandi_api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Find user by phone number — used during login
    Optional<User> findByPhone(String phone);

    // Check if phone already exists — used during registration
    Boolean existsByPhone(String phone);

    // Find all users by role — FARMER, BUYER, ADMIN
    List<User> findByRole(String role);

    // Find all users by status — PENDING, VERIFIED, BLOCKED
    List<User> findByStatus(String status);

    // Find all users by state — for location filtering
    List<User> findByState(String state);
}