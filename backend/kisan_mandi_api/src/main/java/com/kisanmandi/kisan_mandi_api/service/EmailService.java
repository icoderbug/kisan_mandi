package com.kisanmandi.kisan_mandi_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Send simple email
    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();;
            message.setFrom("your_gmail@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    // Email to farmer when new bid placed
    public void sendNewBidEmail(String farmerEmail, String farmerName,
                                String cropName, Double bidAmount,
                                String buyerName) {
        String subject = "🔨 New bid on your " + cropName + " listing!";
        String body =
                "Dear " + farmerName + ",\n\n" +
                        "Great news! A new bid has been placed on your crop listing.\n\n" +
                        "Crop: " + cropName + "\n" +
                        "Bid Amount: ₹" + bidAmount + "/qtl\n" +
                        "Buyer: " + buyerName + "\n\n" +
                        "Login to Kisan Mandi to view all bids and accept the best offer.\n\n" +
                        "Best regards,\n" +
                        "Kisan Mandi Team";
        sendEmail(farmerEmail, subject, body);
    }

    // Email to buyer when they are outbid
    public void sendOutbidEmail(String buyerEmail, String buyerName,
                                String cropName, Double newHighestBid) {
        String subject = "⚠️ You have been outbid on " + cropName;
        String body =
                "Dear " + buyerName + ",\n\n" +
                        "Someone has placed a higher bid on a crop you are bidding on.\n\n" +
                        "Crop: " + cropName + "\n" +
                        "New Highest Bid: ₹" + newHighestBid + "/qtl\n\n" +
                        "Login to Kisan Mandi to raise your bid before the auction closes!\n\n" +
                        "Best regards,\n" +
                        "Kisan Mandi Team";
        sendEmail(buyerEmail, subject, body);
    }

    // Email to buyer when they win a bid
    public void sendBidWonEmail(String buyerEmail, String buyerName,
                                String cropName, Double finalPrice,
                                String orderNumber) {
        String subject = "🎉 Congratulations! You won the bid for " + cropName;
        String body =
                "Dear " + buyerName + ",\n\n" +
                        "Congratulations! Your bid has been accepted by the farmer.\n\n" +
                        "Order Details:\n" +
                        "Crop: " + cropName + "\n" +
                        "Final Price: ₹" + finalPrice + "/qtl\n" +
                        "Order Number: " + orderNumber + "\n\n" +
                        "Please login to Kisan Mandi to complete your payment.\n\n" +
                        "Best regards,\n" +
                        "Kisan Mandi Team";
        sendEmail(buyerEmail, subject, body);
    }

    // Email to farmer when payment received
    public void sendPaymentReceivedEmail(String farmerEmail, String farmerName,
                                         String cropName, Double amount,
                                         String orderNumber) {
        String subject = "💰 Payment received for " + cropName;
        String body =
                "Dear " + farmerName + ",\n\n" +
                        "Great news! Payment has been received for your crop.\n\n" +
                        "Order Details:\n" +
                        "Crop: " + cropName + "\n" +
                        "Amount Received: ₹" + amount + "\n" +
                        "Order Number: " + orderNumber + "\n\n" +
                        "Please ship the crop to the buyer at the earliest.\n\n" +
                        "Best regards,\n" +
                        "Kisan Mandi Team";
        sendEmail(farmerEmail, subject, body);
    }

    // Email to buyer when order shipped
    public void sendOrderShippedEmail(String buyerEmail, String buyerName,
                                      String cropName, String farmerName,
                                      String farmerLocation) {
        String subject = "🚚 Your order for " + cropName + " has been shipped!";
        String body =
                "Dear " + buyerName + ",\n\n" +
                        "Your order has been shipped by the farmer.\n\n" +
                        "Crop: " + cropName + "\n" +
                        "Shipped by: " + farmerName + "\n" +
                        "From: " + farmerLocation + "\n\n" +
                        "Your order will be delivered soon.\n\n" +
                        "Best regards,\n" +
                        "Kisan Mandi Team";
        sendEmail(buyerEmail, subject, body);
    }
}