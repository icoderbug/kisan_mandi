package com.kisanmandi.kisan_mandi_api.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

// This is what frontend sends when farmer lists a crop
@Data
public class CropRequest {
    private String name;
    private String category;
    private Double quantity;
    private String unit;
    private String description;
    private Double basePrice;
    private LocalDateTime harvestDate;
    private LocalDateTime bidEndTime;
    private String village;
    private String district;
    private String state;
    private List<String> photos;
}