package com.kisanmandi.kisan_mandi_api.controller;

import com.kisanmandi.kisan_mandi_api.dto.CropRequest;
import com.kisanmandi.kisan_mandi_api.model.Crop;
import com.kisanmandi.kisan_mandi_api.service.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "*")
public class CropController {

    @Autowired
    private CropService cropService;

    // GET /api/crops/browse — public, no token needed
    @GetMapping("/browse")
    public ResponseEntity<List<Crop>> browseCrops(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String search) {
        try {
            List<Crop> crops;
            if (search != null && !search.isEmpty()) {
                crops = cropService.searchCrops(search);
            } else if (state != null && !state.isEmpty()) {
                crops = cropService.getCropsByState(state);
            } else {
                crops = cropService.getAllActiveCrops();
            }
            return ResponseEntity.ok(crops);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/crops/{id} — get single crop
    @GetMapping("/{id}")
    public ResponseEntity<Crop> getCropById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(cropService.getCropById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/crops/add — farmer adds crop
    @PostMapping("/add")
    public ResponseEntity<?> addCrop(@RequestBody CropRequest request) {
        try {
            String farmerId = getUserId();
            Crop crop = cropService.addCrop(request, farmerId);
            return ResponseEntity.ok(crop);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/crops/my/listings — farmer sees own crops
    @GetMapping("/my/listings")
    public ResponseEntity<List<Crop>> myListings() {
        try {
            String farmerId = getUserId();
            return ResponseEntity.ok(cropService.getFarmerCrops(farmerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE /api/crops/{id} — farmer deletes crop
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCrop(@PathVariable String id) {
        try {
            String farmerId = getUserId();
            cropService.deleteCrop(id, farmerId);
            return ResponseEntity.ok("Crop deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PATCH /api/crops/{id}/sold — farmer marks crop as sold
    @PatchMapping("/{id}/sold")
    public ResponseEntity<?> markAsSold(@PathVariable String id) {
        try {
            Crop crop = cropService.markAsSold(id);
            return ResponseEntity.ok(crop);
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