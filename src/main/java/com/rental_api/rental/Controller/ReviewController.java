package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import com.rental_api.rental.Dtos.Response.ApiResponse;
import com.rental_api.rental.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Create a new review
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @RequestBody ReviewRequest request,
            Authentication auth
    ) {
        ReviewResponse res = reviewService.createReview(request, auth);
        return ResponseEntity
                .status(201)
                .body(ApiResponse.success(201, "Review created successfully", res));
    }

    // Update an existing review
    @PutMapping("/{propertyId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long propertyId,
            @RequestBody ReviewRequest request,
            Authentication auth
    ) {
        ReviewResponse res = reviewService.updateReview(propertyId, request, auth);
        return ResponseEntity
                .ok(ApiResponse.success(200, "Review updated successfully", res));
    }
}
