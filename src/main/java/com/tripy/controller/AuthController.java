package com.tripy.controller;

import com.tripy.dto.TripyDTOs;
import com.tripy.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<TripyDTOs.ApiResponse> register(@Valid @RequestBody TripyDTOs.RegisterRequest request) {
        TripyDTOs.AuthResponse authResponse = authService.register(request);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("User registered successfully", authResponse));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<TripyDTOs.ApiResponse> login(@Valid @RequestBody TripyDTOs.LoginRequest request) {
        TripyDTOs.AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Login successful", authResponse));
    }
}
