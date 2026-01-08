package com.rental_api.rental.Services;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import org.springframework.security.core.Authentication;

public interface ReviewService {
    ReviewResponse createReview(ReviewRequest request, Authentication auth);
        ReviewResponse updateReview(Long propertyId, ReviewRequest request, Authentication auth);

}
