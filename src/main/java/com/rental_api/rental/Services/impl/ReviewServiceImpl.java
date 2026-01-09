package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Exception.ConflictException;
import com.rental_api.rental.Exception.PropertyNotFoundException;
import com.rental_api.rental.Exception.UserNotFoundException;
import com.rental_api.rental.Repository.PropertyRepository;
import com.rental_api.rental.Repository.ReviewRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marks this class as a Spring service
@RequiredArgsConstructor // Auto-injects final fields via constructor
@Transactional // Enables transaction management for all methods
public class ReviewServiceImpl implements ReviewService {

    // Repository to manage Review database operations
    private final ReviewRepository reviewRepository;

    // Repository to manage Property database operations
    private final PropertyRepository propertyRepository;

    // Repository to manage User database operations
    private final UserRepository userRepository;

    // ================= CREATE REVIEW =================
    @Override
    public ReviewResponse createReview(ReviewRequest request, Authentication auth) {

        // Get currently logged-in user from authentication
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Find property by ID from request
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        // Check if user already reviewed this property
        if (reviewRepository.existsByUserAndProperty(user, property)) {
            throw new ConflictException(
                    "You have already reviewed this property. Use update to change it."
            );
        }

        // Create new Review entity
        Review review = new Review();
        review.setUser(user); // Set review owner
        review.setProperty(property); // Set reviewed property
        review.setRating(request.getRating()); // Set rating value
        review.setComment(request.getComment()); // Set review comment

        // Save review to database
        reviewRepository.save(review);

        // Recalculate property average rating and total reviews
        property.updateReviewStats();
        propertyRepository.save(property);

        // Return response DTO
        return mapToResponse(review);
    }

    // ================= UPDATE REVIEW =================
    @Override
    public ReviewResponse updateReview(Long propertyId, ReviewRequest request, Authentication auth) {

        // Get logged-in user
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Find property by ID
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        // Find existing review by user and property
        Review review = reviewRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new ConflictException(
                        "You have not reviewed this property yet."
                ));

        // Update rating and comment
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // Save updated review
        reviewRepository.save(review);

        // Update property review statistics
        property.updateReviewStats();
        propertyRepository.save(property);

        // Return updated review response
        return mapToResponse(review);
    }

    // ================= GET REVIEWS BY PROPERTY =================
    @Override
    @Transactional(readOnly = true) // Read-only for better performance
    public List<ReviewResponse> getReviewsByProperty(Long propertyId) {

        // Find property by ID
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        // Convert all reviews to response DTO list
        return property.getReviews()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET USER REVIEW =================
    @Override
    @Transactional(readOnly = true) // Read-only transaction
    public ReviewResponse getUserReview(Long propertyId, Authentication auth) {

        // Get logged-in user
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Find property by ID
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        // Find review written by this user for this property
        Review review = reviewRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new ConflictException(
                        "You have not reviewed this property yet."
                ));

        // Return user review as response
        return mapToResponse(review);
    }

    // ================= MAPPER =================
    private ReviewResponse mapToResponse(Review review) {

        // Convert Review entity to ReviewResponse DTO
        return new ReviewResponse(
                review.getId(), // Review ID
                review.getProperty().getId(), // Property ID
                review.getUser().getId(), // User ID
                review.getUser().getUsername(), // Username
                review.getRating(), // Rating value
                review.getComment(), // Review comment
                review.getCreatedAt() // Review creation time
        );
    }
}
