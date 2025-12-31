package com.rental_api.rental.Dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private T data; // can hold any response (user info, token, etc.)
    
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), status, "Success", message, data);
    }

    public static <T> ApiResponse<T> error(int status, String error, String message) {
        return new ApiResponse<>(LocalDateTime.now(), status, error, message, null);
    }
}
