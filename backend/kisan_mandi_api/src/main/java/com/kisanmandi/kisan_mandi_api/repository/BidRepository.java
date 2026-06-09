package com.kisanmandi.kisan_mandi_api.repository;

import com.kisanmandi.kisan_mandi_api.model.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends MongoRepository<Bid, String> {

    // Get all bids for a specific crop
    List<Bid> findByCropId(String cropId);

    // Get all bids placed by a specific buyer
    List<Bid> findByBuyerId(String buyerId);

    // Get highest bid for a crop
    Optional<Bid> findTopByCropIdOrderByAmountDesc(String cropId);

    // Get all bids for a farmer's crops
    List<Bid> findByFarmerId(String farmerId);

    // Get all active bids by buyer
    List<Bid> findByBuyerIdAndStatus(String buyerId, String status);
}