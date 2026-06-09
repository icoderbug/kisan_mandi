package com.kisanmandi.kisan_mandi_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

// Maps to "bids" collection in MongoDB
@Document(collection = "bids")
@Data
public class Bid {

    @Id
    private String id;

    // Which crop this bid is for
    private String cropId;
    private String cropName;

    // Who placed the bid
    private String buyerId;
    private String buyerName;
    private String buyerLocation;

    // Who owns the crop
    private String farmerId;

    // Bid amount
    private Double amount;

    // Is this the highest bid right now?
    private Boolean isHighest;

    // Status — ACTIVE, WON, LOST, WITHDRAWN
    private String status;

    // When was this bid placed
    private LocalDateTime placedAt;
}