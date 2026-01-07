package com.rental_api.rental.Dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String error;
    private String message;
    private T data;

    // success
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, null, message, data);
    }

    // error
    public static <T> ApiResponse<T> error(int status, String error, String message) {
        return new ApiResponse<>(status, error, message, null);
    }
}
