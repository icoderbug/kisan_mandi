package com.kisanmandi.kisan_mandi_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

// Maps to "orders" collection in MongoDB
@Document(collection = "orders")
@Data
public class Order {

    @Id
    private String id;

    // Order reference number — like KM-2024-001
    private String orderNumber;

    // Crop details
    private String cropId;
    private String cropName;
    private Double quantity;
    private String unit;

    // Farmer details
    private String farmerId;
    private String farmerName;
    private String farmerLocation;

    // Buyer details
    private String buyerId;
    private String buyerName;
    private String buyerLocation;

    // Pricing
    private Double finalPrice;     // price per qtl
    private Double totalAmount;    // finalPrice × quantity

    // Payment status — PENDING, PAID
    private String paymentStatus;

    // Delivery status — PENDING, SHIPPED, DELIVERED
    private String deliveryStatus;

    // Timestamps
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime createdAt;
}