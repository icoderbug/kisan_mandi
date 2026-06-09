package com.kisanmandi.kisan_mandi_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

// This is what gets broadcast to all connected browsers
@Data
@AllArgsConstructor
public class BidMessage {
    private String cropId;
    private String cropName;
    private String buyerName;
    private String buyerLocation;
    private Double amount;
    private Integer totalBids;
    private String timestamp;
}