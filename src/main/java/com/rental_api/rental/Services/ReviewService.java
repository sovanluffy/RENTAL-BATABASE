package com.rental_api.rental.Services;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ReviewService {

    // Create a new review
    ReviewResponse createReview(ReviewRequest request, Authentication auth);

    // Update existing review
    ReviewResponse updateReview(Long propertyId, ReviewRequest request, Authentication auth);

    // Get all reviews for a property
    List<ReviewResponse> getReviewsByProperty(Long propertyId);
}
