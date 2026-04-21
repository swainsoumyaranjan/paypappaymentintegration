package com.paypal.dto;

import com.paypal.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    
    private UUID id;
    private String orderNumber;
    private BigDecimal amount;
    private String currency;
    private Order.OrderStatus status;
    private String paypalOrderId;
    private String paypalPaymentId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userEmail;
}
