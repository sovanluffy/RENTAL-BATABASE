package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest);
    }
}
