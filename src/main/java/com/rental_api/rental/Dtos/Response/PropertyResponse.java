package com.rental_api.rental.Dtos.Response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PropertyResponse {

    private Long id;
    private String title;
    private String description;
    private String address;
    private Double price;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
