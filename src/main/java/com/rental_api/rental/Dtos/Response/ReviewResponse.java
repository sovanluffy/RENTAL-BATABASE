package com.rental_api.rental.Dtos.Response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {

    private Long id;              // Review ID
    private Long userId;          // Who wrote the review
    private String username;      // Username of reviewer
    private Long propertyId;      // Property ID
    private String propertyTitle; // Property title
    private Integer rating;       // Rating 1-5
    private String comment;       // Review comment
    private LocalDateTime createdAt; // When review was created
}
