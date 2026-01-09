package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ApiResponse;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import com.rental_api.rental.Services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ---------------- CREATE REVIEW ----------------
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication auth
    ) {
        ReviewResponse res = reviewService.createReview(request, auth);
        return ResponseEntity.status(201)
                .body(ApiResponse.success(201, "Review created successfully", res));
    }

    // ---------------- UPDATE REVIEW ----------------
    @PutMapping("/{propertyId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long propertyId,
            @Valid @RequestBody ReviewRequest request,
            Authentication auth
    ) {
        ReviewResponse res = reviewService.updateReview(propertyId, request, auth);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Review updated successfully", res)
        );
    }

    // ---------------- GET REVIEWS BY PROPERTY ----------------
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<ApiResponse<Object>> getReviewsByProperty(
            @PathVariable Long propertyId
    ) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProperty(propertyId);

        int totalReviews = reviews.size();
        double avgRating = reviews.stream()
                .mapToInt(ReviewResponse::getRating)
                .average()
                .orElse(0.0);

        Map<String, Object> response = Map.of(
                "totalReviews", totalReviews,
                "avgRating", avgRating,
                "reviews", reviews
        );

        return ResponseEntity.ok(
                ApiResponse.success(200, "Property reviews fetched successfully", response)
        );
    }

    // ---------------- GET LOGGED-IN USER REVIEW ----------------
    @GetMapping("/{propertyId}/user")
    public ResponseEntity<ApiResponse<ReviewResponse>> getUserReview(
            @PathVariable Long propertyId,
            Authentication auth
    ) {
        ReviewResponse res = reviewService.getUserReview(propertyId, auth);
        return ResponseEntity.ok(
                ApiResponse.success(200, "User review fetched successfully", res)
        );
    }
}
