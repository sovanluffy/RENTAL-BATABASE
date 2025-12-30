package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Request.RegisterRequest;
import com.rental_api.rental.Dtos.Response.RegisterResponse;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Services.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest);
    }


     @PostMapping("/register")
	     public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) throws Exception{
	    	RegisterResponse response = authService.register(req);
	    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }

};
