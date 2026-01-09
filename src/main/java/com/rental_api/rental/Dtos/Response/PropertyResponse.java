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

    // 👤 Agent info
    private Long userId;
    private String username;

    // ⭐ Review stats
    private Integer totalReviews;
    private Double avgRating;

    // 📸 Images
    private List<String> images;

    // 🕒 Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
