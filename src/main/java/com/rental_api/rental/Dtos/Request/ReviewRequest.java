package com.rental_api.rental.Dtos.Request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long propertyId;   // ✅ property to review
    private Integer rating;
    private String comment;
}
