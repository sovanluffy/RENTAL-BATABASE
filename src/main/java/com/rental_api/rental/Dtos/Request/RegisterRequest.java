package com.rental_api.rental.Dtos.Request;

    
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private Long roleId;
    private String first_name;
    private String Last_name;
    private String username;
    private String email;
    private String phone;
    private String password;
}