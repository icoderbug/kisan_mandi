package com.kisanmandi.kisan_mandi_api.service;

import com.kisanmandi.kisan_mandi_api.model.Bid;
import com.kisanmandi.kisan_mandi_api.model.Crop;
import com.kisanmandi.kisan_mandi_api.model.Order;
import com.kisanmandi.kisan_mandi_api.model.User;
import com.kisanmandi.kisan_mandi_api.repository.BidRepository;
import com.kisanmandi.kisan_mandi_api.repository.CropRepository;
import com.kisanmandi.kisan_mandi_api.repository.OrderRepository;
import com.kisanmandi.kisan_mandi_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    // Farmer accepts highest bid — creates an order
    public Order acceptBid(String cropId, String farmerId) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found!"));

        if (!crop.getFarmerId().equals(farmerId)) {
            throw new RuntimeException("You can only accept bids on your own crops!");
        }

        // Get highest bid
        Bid highestBid = bidRepository
                .findTopByCropIdOrderByAmountDesc(cropId)
                .orElseThrow(() -> new RuntimeException("No bids found!"));

        User buyer = userRepository.findById(highestBid.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found!"));

        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found!"));

        // Create order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCropId(cropId);
        order.setCropName(crop.getName());
        order.setQuantity(crop.getQuantity());
        order.setUnit(crop.getUnit());
        order.setFarmerId(farmerId);
        order.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
        order.setFarmerLocation(farmer.getState());
        order.setBuyerId(highestBid.getBuyerId());
        order.setBuyerName(buyer.getFirstName() + " " + buyer.getLastName());
        order.setBuyerLocation(buyer.getState());
        order.setFinalPrice(highestBid.getAmount());
        order.setTotalAmount(highestBid.getAmount() * crop.getQuantity());
        order.setPaymentStatus("PENDING");
        order.setDeliveryStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());

        // Mark crop as sold
        crop.setStatus("SOLD");
        cropRepository.save(crop);

        // Mark winning bid
        highestBid.setStatus("WON");
        bidRepository.save(highestBid);

        // Email to buyer — bid won
        if (buyer.getEmail() != null) {
            emailService.sendBidWonEmail(
                    buyer.getEmail(),
                    buyer.getFirstName(),
                    crop.getName(),
                    highestBid.getAmount(),
                    order.getOrderNumber()
            );
        }

        return orderRepository.save(order);
    }

    // Get all orders for a farmer
    public List<Order> getFarmerOrders(String farmerId) {
        return orderRepository.findByFarmerId(farmerId);
    }

    // Get all orders for a buyer
    public List<Order> getBuyerOrders(String buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    // Update payment status
    public Order updatePaymentStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setPaymentStatus(status);
        if (status.equals("PAID")) {
            order.setPaymentDate(LocalDateTime.now());
        }
        // Email to farmer — payment received
        if (status.equals("PAID")) {
            User farmer = userRepository.findById(order.getFarmerId()).orElse(null);
            if (farmer != null && farmer.getEmail() != null) {
                emailService.sendPaymentReceivedEmail(
                        farmer.getEmail(),
                        farmer.getFirstName(),
                        order.getCropName(),
                        order.getTotalAmount(),
                        order.getOrderNumber()
                );
            }
        }
        return orderRepository.save(order);
    }

    // Update delivery status
    public Order updateDeliveryStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setDeliveryStatus(status);
        if (status.equals("DELIVERED")) {
            order.setDeliveryDate(LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

    // Generate unique order number
    private String generateOrderNumber() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String random = String.format("%03d", new Random().nextInt(1000));
        return "KM-" + year + "-" + random;
    }
}