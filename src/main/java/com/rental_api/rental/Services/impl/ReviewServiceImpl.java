package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Exception.UnauthorizedException;
import com.rental_api.rental.Exception.UserNotFoundException;
import com.rental_api.rental.Repository.PropertyRepository;
import com.rental_api.rental.Repository.ReviewRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse addOrUpdateReview(Long propertyId, ReviewRequest request, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Check if user already reviewed
        Review review = reviewRepository.findByUserAndProperty(user, property)
                .orElse(new Review());

        review.setUser(user);
        review.setProperty(property);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);

        return mapToResponse(saved);
    }

    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        response.setPropertyId(review.getProperty().getId());
        response.setUsername(review.getUser().getUsername());
        return response;
    }
}
