package com.paypal.controller;

import com.paypal.core.PayPalHttpClient;
import com.paypal.dto.*;
import com.paypal.exception.PaymentException;
import com.paypal.exception.ResourceNotFoundException;
import com.paypal.model.Order;
import com.paypal.model.Transaction;
import com.paypal.repository.OrderRepository;
import com.paypal.repository.TransactionRepository;
import com.paypal.service.PayPalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final PayPalService payPalService;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;
    
    @Value("${paypal.mode}")
    private String paypalMode;
    
    public PaymentController(PayPalService payPalService, OrderRepository orderRepository,
                            TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.payPalService = payPalService;
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }
    
    @PostMapping("/paypal/create-order")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> createPayPalOrder(@RequestBody OrderRequestDTO requestDTO) {
        try {
            // Create order in database first
            Order order = new Order();
            order.setOrderNumber("ORD-" + System.currentTimeMillis());
            order.setAmount(requestDTO.getAmount());
            order.setCurrency("USD");
            order.setDescription(requestDTO.getDescription());
            order.setStatus(Order.OrderStatus.PENDING);
            order = orderRepository.save(order);
            
            // Create PayPal order
            String frontendUrl = "http://localhost:3000";
            com.paypal.orders.Order paypalOrder = payPalService.createOrder(
                    requestDTO.getAmount(),
                    "USD",
                    requestDTO.getDescription(),
                    frontendUrl + "/payment/success?orderId=" + order.getId(),
                    frontendUrl + "/payment/cancel?orderId=" + order.getId()
            );
            
            // Update order with PayPal order ID
            order.setPaypalOrderId(paypalOrder.id());
            orderRepository.save(order);
            
            // Get approve URL
            String approveUrl = paypalOrder.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .map(com.paypal.orders.Link::href)
                    .findFirst()
                    .orElse(null);
            
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setOrderId(order.getId().toString());
            response.setPaypalOrderId(paypalOrder.id());
            response.setApproveUrl(approveUrl);
            response.setStatus(paypalOrder.status());
            
            return ResponseEntity.ok(ApiResponseDTO.success(response, "PayPal order created successfully"));
            
        } catch (Exception e) {
            throw new PaymentException("Failed to create PayPal order: " + e.getMessage());
        }
    }
    
    @PostMapping("/paypal/capture/{paypalOrderId}")
    public ResponseEntity<ApiResponseDTO<Void>> capturePayment(@PathVariable String paypalOrderId) {
        try {
            // Capture PayPal order
            com.paypal.orders.Order capturedOrder = payPalService.captureOrder(paypalOrderId);
            
            // Find order in database
            Order order = orderRepository.findByPaypalOrderId(paypalOrderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            
            // Update order status
            order.setStatus(Order.OrderStatus.COMPLETED);
            order.setPaypalPaymentId(capturedOrder.id());
            orderRepository.save(order);
            
            // Create transaction record
            Transaction transaction = new Transaction();
            transaction.setOrder(order);
            transaction.setTransactionId(capturedOrder.id());
            transaction.setType(Transaction.TransactionType.PAYMENT);
            transaction.setAmount(order.getAmount());
            transaction.setStatus(capturedOrder.status());
            transaction.setTimestamp(LocalDateTime.now());
            
            try {
                transaction.setResponsePayload(objectMapper.writeValueAsString(capturedOrder));
            } catch (Exception e) {
                // Ignore JSON conversion errors
            }
            
            transactionRepository.save(transaction);
            
            return ResponseEntity.ok(ApiResponseDTO.success(null, "Payment captured successfully"));
            
        } catch (Exception e) {
            throw new PaymentException("Failed to capture payment: " + e.getMessage());
        }
    }
    
    @PostMapping("/paypal/refund/{transactionId}")
    public ResponseEntity<ApiResponseDTO<Void>> refundPayment(
            @PathVariable String transactionId,
            @RequestBody RefundRequestDTO refundRequest) {
        try {
            // Find transaction
            Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
            
            // Process refund through PayPal
            com.paypal.payments.Refund refund = payPalService.refundPayment(
                    transactionId,
                    refundRequest.getAmount(),
                    transaction.getOrder().getCurrency()
            );
            
            // Update order status
            Order order = transaction.getOrder();
            order.setStatus(Order.OrderStatus.REFUNDED);
            orderRepository.save(order);
            
            // Create refund transaction
            Transaction refundTransaction = new Transaction();
            refundTransaction.setOrder(order);
            refundTransaction.setTransactionId(refund.id());
            refundTransaction.setType(Transaction.TransactionType.REFUND);
            refundTransaction.setAmount(refundRequest.getAmount());
            refundTransaction.setStatus(refund.status());
            refundTransaction.setTimestamp(LocalDateTime.now());
            
            try {
                refundTransaction.setResponsePayload(objectMapper.writeValueAsString(refund));
            } catch (Exception e) {
                // Ignore JSON conversion errors
            }
            
            transactionRepository.save(refundTransaction);
            
            return ResponseEntity.ok(ApiResponseDTO.success(null, "Refund processed successfully"));
            
        } catch (Exception e) {
            throw new PaymentException("Failed to process refund: " + e.getMessage());
        }
    }
    
    @GetMapping("/paypal/details/{paypalOrderId}")
    public ResponseEntity<ApiResponseDTO<Object>> getPaymentDetails(@PathVariable String paypalOrderId) {
        try {
            com.paypal.orders.Order order = payPalService.getOrderDetails(paypalOrderId);
            return ResponseEntity.ok(ApiResponseDTO.success(order, "Payment details retrieved successfully"));
        } catch (Exception e) {
            throw new PaymentException("Failed to get payment details: " + e.getMessage());
        }
    }
}
