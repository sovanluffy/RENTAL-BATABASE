package com.rental_api.rental.Services;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Request.RegisterRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;
import com.rental_api.rental.Dtos.Response.RegisterResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest) throws Exception;
    RegisterResponse register (RegisterRequest registerRequest) throws Exception;
};

