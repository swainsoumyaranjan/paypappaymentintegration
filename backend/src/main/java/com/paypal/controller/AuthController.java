package com.paypal.controller;

import com.paypal.dto.ApiResponseDTO;
import com.paypal.dto.LoginDTO;
import com.paypal.dto.UserRegistrationDTO;
import com.paypal.model.User;
import com.paypal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<User>> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User user = authService.registerUser(registrationDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(user, "User registered successfully"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, String> tokenResponse = authService.authenticateUser(loginDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(tokenResponse, "Login successful"));
    }
}
