package com.kisanmandi.kisan_mandi_api.controller;

import com.kisanmandi.kisan_mandi_api.dto.BidRequest;
import com.kisanmandi.kisan_mandi_api.model.Bid;
import com.kisanmandi.kisan_mandi_api.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin(origins = "*")
public class BidController {

    @Autowired
    private BidService bidService;

    // POST /api/bids/place — buyer places a bid
    @PostMapping("/place")
    public ResponseEntity<?> placeBid(@RequestBody BidRequest request) {
        try {
            String buyerId = getUserId();
            Bid bid = bidService.placeBid(request, buyerId);
            return ResponseEntity.ok(bid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/bids/crop/{cropId} — get all bids for a crop
    @GetMapping("/crop/{cropId}")
    public ResponseEntity<List<Bid>> getBidsForCrop(@PathVariable String cropId) {
        try {
            return ResponseEntity.ok(bidService.getBidsForCrop(cropId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/bids/my — buyer sees own bids
    @GetMapping("/my")
    public ResponseEntity<List<Bid>> myBids() {
        try {
            String buyerId = getUserId();
            return ResponseEntity.ok(bidService.getBuyerBids(buyerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/bids/farmer — farmer sees all bids on crops
    @GetMapping("/farmer")
    public ResponseEntity<List<Bid>> farmerBids() {
        try {
            String farmerId = getUserId();
            return ResponseEntity.ok(bidService.getFarmerBids(farmerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE /api/bids/{id} — buyer withdraws bid
    @DeleteMapping("/{id}")
    public ResponseEntity<?> withdrawBid(@PathVariable String id) {
        try {
            String buyerId = getUserId();
            bidService.withdrawBid(id, buyerId);
            return ResponseEntity.ok("Bid withdrawn successfully!");
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