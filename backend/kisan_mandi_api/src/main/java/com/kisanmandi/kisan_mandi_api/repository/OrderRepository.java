package com.kisanmandi.kisan_mandi_api.repository;

import com.kisanmandi.kisan_mandi_api.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // Get all orders for a farmer
    List<Order> findByFarmerId(String farmerId);

    // Get all orders for a buyer
    List<Order> findByBuyerId(String buyerId);

    // Get orders by payment status
    List<Order> findByPaymentStatus(String paymentStatus);

    // Get orders by delivery status
    List<Order> findByDeliveryStatus(String deliveryStatus);

    // Get order by order number
    Order findByOrderNumber(String orderNumber);
}