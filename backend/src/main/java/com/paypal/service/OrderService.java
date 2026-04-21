package com.paypal.service;

import com.paypal.dto.OrderRequestDTO;
import com.paypal.dto.OrderResponseDTO;
import com.paypal.exception.ResourceNotFoundException;
import com.paypal.model.Order;
import com.paypal.model.Transaction;
import com.paypal.model.User;
import com.paypal.repository.OrderRepository;
import com.paypal.repository.TransactionRepository;
import com.paypal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                       TransactionRepository transactionRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @Transactional
    public OrderResponseDTO createOrder(String userEmail, OrderRequestDTO requestDTO) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setAmount(requestDTO.getAmount());
        order.setCurrency("USD");
        order.setDescription(requestDTO.getDescription());
        order.setStatus(Order.OrderStatus.PENDING);
        
        order = orderRepository.save(order);
        
        return mapToOrderResponseDTO(order);
    }
    
    public OrderResponseDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        return mapToOrderResponseDTO(order);
    }
    
    public List<OrderResponseDTO> getOrdersByUser(UUID userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID orderId, Order.OrderStatus status, String paypalOrderId, String paypalPaymentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        order.setStatus(status);
        if (paypalOrderId != null) {
            order.setPaypalOrderId(paypalOrderId);
        }
        if (paypalPaymentId != null) {
            order.setPaypalPaymentId(paypalPaymentId);
        }
        
        order = orderRepository.save(order);
        return mapToOrderResponseDTO(order);
    }
    
    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setAmount(order.getAmount());
        dto.setCurrency(order.getCurrency());
        dto.setStatus(order.getStatus());
        dto.setPaypalOrderId(order.getPaypalOrderId());
        dto.setPaypalPaymentId(order.getPaypalPaymentId());
        dto.setDescription(order.getDescription());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setUserEmail(order.getUser().getEmail());
        return dto;
    }
    
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
