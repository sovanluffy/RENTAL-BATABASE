package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Request.RegisterRequest;
import com.rental_api.rental.Dtos.Response.ApiResponse;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Dtos.Response.RegisterResponse;
import com.rental_api.rental.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse res = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(200, "User registered successfully", res));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse res = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(200, "Login successful", res));
    }
}
