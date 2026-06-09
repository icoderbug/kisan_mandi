package com.kisanmandi.kisan_mandi_api.dto;

import lombok.Data;

// This is what frontend sends when buyer places a bid
@Data
public class BidRequest {
    private String cropId;
    private Double amount;
}