package com.kisanmandi.kisan_mandi_api.repository;

import com.kisanmandi.kisan_mandi_api.model.Crop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CropRepository extends MongoRepository<Crop, String> {

    // Get all crops by a specific farmer
    List<Crop> findByFarmerId(String farmerId);

    // Get all crops by status — ACTIVE, SOLD, EXPIRED
    List<Crop> findByStatus(String status);

    // Get all active crops by state — for location filtering
    List<Crop> findByStatusAndState(String status, String state);

    // Get all crops by name — search feature
    List<Crop> findByNameContainingIgnoreCase(String name);

    // Get all crops by category
    List<Crop> findByCategory(String category);
}