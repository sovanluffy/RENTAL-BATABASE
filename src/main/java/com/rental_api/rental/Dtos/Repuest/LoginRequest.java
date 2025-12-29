package com.rental_api.rental.Dtos.Repuest;

import lombok.Data;

@Data
public class LoginRequest {
    private String usernameOrEmail;
    private String password;
}
