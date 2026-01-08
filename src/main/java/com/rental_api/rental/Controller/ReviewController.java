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

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @RequestBody ReviewRequest request,
            Authentication auth
    ) {
        ReviewResponse res = reviewService.createReview(request, auth);

        // Wrap in ApiResponse
        return ResponseEntity
                .status(201)
                .body(ApiResponse.success(201, "Review created successfully", res));
    }
}
