package com.kisanmandi.kisan_mandi_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

// Maps to "crops" collection in MongoDB
@Document(collection = "crops")
@Data
public class Crop {

    @Id
    private String id;

    // Which farmer listed this crop
    private String farmerId;
    private String farmerName;

    // Crop details
    private String name;        // Wheat, Tomato, Maize etc
    private String category;    // Grain, Vegetable, Fruit etc
    private Double quantity;
    private String unit;        // kg, Quintal, Ton
    private String description;

    // Pricing
    private Double basePrice;   // minimum price set by farmer
    private Double highestBid;  // current highest bid
    private String highestBidderId;

    // Location — where to pick up
    private String village;
    private String district;
    private String state;

    // Photos uploaded by farmer
    private List<String> photos;

    // Bidding time
    private LocalDateTime harvestDate;
    private LocalDateTime bidStartTime;
    private LocalDateTime bidEndTime;

    // Status — ACTIVE, SOLD, EXPIRED
    private String status;

    // Total bids placed
    private Integer totalBids;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}