package com.rental_api.rental.Dtos.Response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    private Double averageRating;
    private Integer totalReviews;

    private List<ReviewDTO> reviews;

    @Data
    public static class ReviewDTO {
        private Long id;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;
    }
}
