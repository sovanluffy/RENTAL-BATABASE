package com.rental_api.rental.Services;

import com.rental_api.rental.Dtos.Repuest.LoginRequest;
import com.rental_api.rental.Dtos.Response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest) throws Exception;
}
