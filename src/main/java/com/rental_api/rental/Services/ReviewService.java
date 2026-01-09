package com.rental_api.rental.Services;

import com.rental_api.rental.Dtos.Request.ReviewRequest; // DTO for creating/updating a review
import com.rental_api.rental.Dtos.Response.ReviewResponse; // DTO for returning review data
import org.springframework.security.core.Authentication; // Holds authenticated user details

import java.util.List; // Used for returning multiple reviews

/**
 * ReviewService defines business logic operations for property reviews
 */
public interface ReviewService {

    // Creates a new review for a property by the authenticated user
    ReviewResponse createReview(ReviewRequest request, Authentication auth);

    // Updates an existing review for a property by the authenticated user
    ReviewResponse updateReview(Long propertyId, ReviewRequest request, Authentication auth);

    // Retrieves all reviews associated with a specific property
    List<ReviewResponse> getReviewsByProperty(Long propertyId);

    // Retrieves the authenticated user's review for a specific property
    ReviewResponse getUserReview(Long propertyId, Authentication auth);
}
