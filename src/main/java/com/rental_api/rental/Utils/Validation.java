package com.rental_api.rental.Utils;

import com.rental_api.rental.Dtos.Request.LoginRequest;
import com.rental_api.rental.Dtos.Request.RegisterRequest;
import com.rental_api.rental.Exception.BadRequestException;

public class Validation {

    // Validate RegisterRequest
    public static void validateRegister(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new BadRequestException("Username is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }

        // Must contain '@' and domain like example.com
        if (!request.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BadRequestException("Email must contain '@' and a valid domain (like example.com)");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        if (request.getPassword().length() < 6 || request.getPassword().length() > 100) {
            throw new BadRequestException("Password must be 6-100 characters");
        }

        if (request.getFirstName() != null && request.getFirstName().length() > 50) {
            throw new BadRequestException("First name must be at most 50 characters");
        }

        if (request.getLastName() != null && request.getLastName().length() > 50) {
            throw new BadRequestException("Last name must be at most 50 characters");
        }

        if (request.getPhone() != null && request.getPhone().length() > 20) {
            throw new BadRequestException("Phone number must be at most 20 characters");
        }
    }

    // Validate LoginRequest
    public static void validateLogin(LoginRequest request) {
        if ((request.getUsername() == null || request.getUsername().isBlank()) &&
            (request.getEmail() == null || request.getEmail().isBlank())) {
            throw new BadRequestException("Username or email must be provided");
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new BadRequestException("Email must contain '@' and a valid domain (like example.com)");
            }
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
    }
}
