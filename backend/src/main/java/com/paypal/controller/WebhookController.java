package com.paypal.controller;

import com.paypal.dto.ApiResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {
    
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    
    @PostMapping("/paypal")
    public ResponseEntity<ApiResponseDTO<Void>> handlePayPalWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String eventType = (String) payload.get("event_type");
            logger.info("Received PayPal webhook event: {}", eventType);
            
            // Handle different webhook events
            switch (eventType != null ? eventType : "") {
                case "PAYMENT.CAPTURE.COMPLETED":
                    logger.info("Payment completed: {}", payload);
                    // Update order status in database
                    break;
                    
                case "PAYMENT.CAPTURE.REFUNDED":
                    logger.info("Payment refunded: {}", payload);
                    // Update order status in database
                    break;
                    
                case "ORDER.APPROVED":
                    logger.info("Order approved: {}", payload);
                    // Update order status in database
                    break;
                    
                default:
                    logger.info("Unhandled event type: {}", eventType);
            }
            
            return ResponseEntity.ok(ApiResponseDTO.success(null, "Webhook received"));
            
        } catch (Exception e) {
            logger.error("Error processing webhook: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponseDTO.error("Error processing webhook"));
        }
    }
}
