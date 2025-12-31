package com.rental_api.rental.Dtos.Response;

import lombok.Data;

@Data
public class RegisterResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String roles; // Comma-separated
    private String message;
}
