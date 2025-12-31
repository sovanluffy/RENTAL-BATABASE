package com.rental_api.rental.Dtos.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private List<Long> roleIds; // Optional extra roles
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String password;
}
