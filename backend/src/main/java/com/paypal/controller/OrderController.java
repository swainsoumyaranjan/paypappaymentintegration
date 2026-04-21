package com.paypal.controller;

import com.paypal.dto.ApiResponseDTO;
import com.paypal.dto.OrderRequestDTO;
import com.paypal.dto.OrderResponseDTO;
import com.paypal.model.User;
import com.paypal.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        String userEmail = getCurrentUserEmail();
        OrderResponseDTO order = orderService.createOrder(userEmail, requestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(order, "Order created successfully"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrder(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(order, "Order retrieved successfully"));
    }
    
    @GetMapping("/user")
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<OrderResponseDTO> orders = orderService.getOrdersByUser(user.getId());
        return ResponseEntity.ok(ApiResponseDTO.success(orders, "Orders retrieved successfully"));
    }
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponseDTO.success(orders, "All orders retrieved successfully"));
    }
    
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
