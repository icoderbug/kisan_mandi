package com.kisanmandi.kisan_mandi_api.controller;

import com.kisanmandi.kisan_mandi_api.model.User;
import com.kisanmandi.kisan_mandi_api.model.Order;
import com.kisanmandi.kisan_mandi_api.repository.UserRepository;
import com.kisanmandi.kisan_mandi_api.repository.OrderRepository;
import com.kisanmandi.kisan_mandi_api.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CropRepository cropRepository;

    // GET /api/admin/stats — platform overview
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFarmers", userRepository.findByRole("FARMER").size());
        stats.put("totalBuyers",  userRepository.findByRole("BUYER").size());
        stats.put("activeListings", cropRepository.findByStatus("ACTIVE").size());
        stats.put("totalOrders", orderRepository.count());
        return ResponseEntity.ok(stats);
    }

    // GET /api/admin/farmers — all farmers
    @GetMapping("/farmers")
    public ResponseEntity<List<User>> getAllFarmers() {
        return ResponseEntity.ok(userRepository.findByRole("FARMER"));
    }

    // GET /api/admin/buyers — all buyers
    @GetMapping("/buyers")
    public ResponseEntity<List<User>> getAllBuyers() {
        return ResponseEntity.ok(userRepository.findByRole("BUYER"));
    }

    // GET /api/admin/pending — pending verifications
    @GetMapping("/pending")
    public ResponseEntity<List<User>> getPendingUsers() {
        return ResponseEntity.ok(userRepository.findByStatus("PENDING"));
    }

    // PATCH /api/admin/verify/{userId} — approve user
    @PatchMapping("/verify/{userId}")
    public ResponseEntity<?> verifyUser(@PathVariable String userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found!"));
            user.setStatus("VERIFIED");
            userRepository.save(user);
            return ResponseEntity.ok("User verified successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PATCH /api/admin/block/{userId} — block user
    @PatchMapping("/block/{userId}")
    public ResponseEntity<?> blockUser(@PathVariable String userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found!"));
            user.setStatus("BLOCKED");
            userRepository.save(user);
            return ResponseEntity.ok("User blocked successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/admin/orders — all orders
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
}