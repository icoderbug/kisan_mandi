package com.kisanmandi.kisan_mandi_api.service;

import com.kisanmandi.kisan_mandi_api.dto.BidRequest;
import com.kisanmandi.kisan_mandi_api.model.Bid;
import com.kisanmandi.kisan_mandi_api.model.Crop;
import com.kisanmandi.kisan_mandi_api.model.User;
import com.kisanmandi.kisan_mandi_api.repository.BidRepository;
import com.kisanmandi.kisan_mandi_api.repository.CropRepository;
import com.kisanmandi.kisan_mandi_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    // Buyer places a bid
    public Bid placeBid(BidRequest request, String buyerId) {
        Crop crop = cropRepository.findById(request.getCropId())
                .orElseThrow(() -> new RuntimeException("Crop not found!"));

        // Check if crop is still active
        if (!crop.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Bidding is closed for this crop!");
        }

        // Check if bid is higher than current highest
        if (request.getAmount() <= crop.getHighestBid()) {
            throw new RuntimeException("Bid must be higher than current highest bid of ₹" + crop.getHighestBid());
        }

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found!"));

        // Mark previous highest bid as not highest
        bidRepository.findByCropId(request.getCropId())
                .forEach(b -> {
                    b.setIsHighest(false);
                    bidRepository.save(b);
                });

        // Create new bid
        Bid bid = new Bid();
        bid.setCropId(request.getCropId());
        bid.setCropName(crop.getName());
        bid.setBuyerId(buyerId);
        bid.setBuyerName(buyer.getFirstName() + " " + buyer.getLastName());
        bid.setBuyerLocation(buyer.getState());
        bid.setFarmerId(crop.getFarmerId());
        bid.setAmount(request.getAmount());
        bid.setIsHighest(true);
        bid.setStatus("ACTIVE");
        bid.setPlacedAt(LocalDateTime.now());

        // Update crop highest bid
        crop.setHighestBid(request.getAmount());
        crop.setHighestBidderId(buyerId);
        crop.setTotalBids(crop.getTotalBids() + 1);
        crop.setUpdatedAt(LocalDateTime.now());
        cropRepository.save(crop);

        // Send email to farmer about new bid
        User farmer = userRepository.findById(crop.getFarmerId())
                .orElse(null);
        if (farmer != null && farmer.getEmail() != null) {
            emailService.sendNewBidEmail(
                    farmer.getEmail(),
                    farmer.getFirstName(),
                    crop.getName(),
                    request.getAmount(),
                    buyer.getFirstName() + " " + buyer.getLastName()
            );
        }

// Send outbid email to previous highest bidder
        if (crop.getHighestBidderId() != null &&
                !crop.getHighestBidderId().equals(buyerId)) {
            User previousBidder = userRepository
                    .findById(crop.getHighestBidderId())
                    .orElse(null);
            if (previousBidder != null && previousBidder.getEmail() != null) {
                emailService.sendOutbidEmail(
                        previousBidder.getEmail(),
                        previousBidder.getFirstName(),
                        crop.getName(),
                        request.getAmount()
                );
            }
        }

        return bidRepository.save(bid);
    }

    // Get all bids for a crop — farmer sees this
    public List<Bid> getBidsForCrop(String cropId) {
        return bidRepository.findByCropId(cropId);
    }

    // Get all bids by a buyer
    public List<Bid> getBuyerBids(String buyerId) {
        return bidRepository.findByBuyerId(buyerId);
    }

    // Get all bids on farmer's crops
    public List<Bid> getFarmerBids(String farmerId) {
        return bidRepository.findByFarmerId(farmerId);
    }

    // Withdraw a bid
    public void withdrawBid(String bidId, String buyerId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found!"));
        if (!bid.getBuyerId().equals(buyerId)) {
            throw new RuntimeException("You can only withdraw your own bids!");
        }
        bid.setStatus("WITHDRAWN");
        bidRepository.save(bid);
    }
}