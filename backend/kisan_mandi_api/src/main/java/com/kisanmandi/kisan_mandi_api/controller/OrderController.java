package com.kisanmandi.kisan_mandi_api.controller;

import com.kisanmandi.kisan_mandi_api.model.Order;
import com.kisanmandi.kisan_mandi_api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // POST /api/orders/accept/{cropId} — farmer accepts highest bid
    @PostMapping("/accept/{cropId}")
    public ResponseEntity<?> acceptBid(@PathVariable String cropId) {
        try {
            String farmerId = getUserId();
            Order order = orderService.acceptBid(cropId, farmerId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/orders/farmer — farmer sees own orders
    @GetMapping("/farmer")
    public ResponseEntity<List<Order>> farmerOrders() {
        try {
            String farmerId = getUserId();
            return ResponseEntity.ok(orderService.getFarmerOrders(farmerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/orders/buyer — buyer sees own orders
    @GetMapping("/buyer")
    public ResponseEntity<List<Order>> buyerOrders() {
        try {
            String buyerId = getUserId();
            return ResponseEntity.ok(orderService.getBuyerOrders(buyerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PATCH /api/orders/{id}/payment — update payment status
    @PatchMapping("/{id}/payment")
    public ResponseEntity<?> updatePayment(
            @PathVariable String id,
            @RequestParam String status) {
        try {
            Order order = orderService.updatePaymentStatus(id, status);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PATCH /api/orders/{id}/delivery — update delivery status
    @PatchMapping("/{id}/delivery")
    public ResponseEntity<?> updateDelivery(
            @PathVariable String id,
            @RequestParam String status) {
        try {
            Order order = orderService.updateDeliveryStatus(id, status);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Helper — get current logged in user id from JWT
    private String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (String) auth.getCredentials();
    }
}