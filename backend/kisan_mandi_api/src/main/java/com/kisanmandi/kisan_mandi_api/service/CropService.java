package com.kisanmandi.kisan_mandi_api.service;

import com.kisanmandi.kisan_mandi_api.dto.CropRequest;
import com.kisanmandi.kisan_mandi_api.model.Crop;
import com.kisanmandi.kisan_mandi_api.model.User;
import com.kisanmandi.kisan_mandi_api.repository.CropRepository;
import com.kisanmandi.kisan_mandi_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private UserRepository userRepository;

    // Farmer adds a new crop listing
    public Crop addCrop(CropRequest request, String farmerId) {
        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found!"));

        Crop crop = new Crop();
        crop.setFarmerId(farmerId);
        crop.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
        crop.setName(request.getName());
        crop.setCategory(request.getCategory());
        crop.setQuantity(request.getQuantity());
        crop.setUnit(request.getUnit());
        crop.setDescription(request.getDescription());
        crop.setBasePrice(request.getBasePrice());
        crop.setHighestBid(request.getBasePrice());
        crop.setVillage(request.getVillage());
        crop.setDistrict(request.getDistrict());
        crop.setState(request.getState());
        crop.setHarvestDate(request.getHarvestDate());
        crop.setBidStartTime(LocalDateTime.now());
        crop.setBidEndTime(request.getBidEndTime());
        crop.setStatus("ACTIVE");
        crop.setTotalBids(0);
        crop.setPhotos(request.getPhotos());
        crop.setCreatedAt(LocalDateTime.now());
        crop.setUpdatedAt(LocalDateTime.now());

        return cropRepository.save(crop);
    }

    // Get all active crops — for buyer browse page
    public List<Crop> getAllActiveCrops() {
        return cropRepository.findByStatus("ACTIVE");
    }

    // Get all active crops by state — location filter
    public List<Crop> getCropsByState(String state) {
        return cropRepository.findByStatusAndState("ACTIVE", state);
    }

    // Get all crops by a farmer
    public List<Crop> getFarmerCrops(String farmerId) {
        return cropRepository.findByFarmerId(farmerId);
    }

    // Get single crop by id
    public Crop getCropById(String cropId) {
        return cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found!"));
    }

    // Search crops by name
    public List<Crop> searchCrops(String name) {
        return cropRepository.findByNameContainingIgnoreCase(name);
    }

    // Delete a crop listing
    public void deleteCrop(String cropId, String farmerId) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found!"));
        if (!crop.getFarmerId().equals(farmerId)) {
            throw new RuntimeException("You can only delete your own crops!");
        }
        cropRepository.deleteById(cropId);
    }

    // Mark crop as sold
    public Crop markAsSold(String cropId) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found!"));
        crop.setStatus("SOLD");
        crop.setUpdatedAt(LocalDateTime.now());
        return cropRepository.save(crop);
    }
}