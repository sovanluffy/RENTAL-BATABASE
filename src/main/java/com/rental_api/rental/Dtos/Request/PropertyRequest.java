package com.rental_api.rental.Dtos.Request;

import lombok.Data;

@Data
public class PropertyRequest {
    private String title;
    private String description;
    private String address;
    private Integer totalReviews;
    private Double avgRating;
    private Double price; // ✅ Double
}
