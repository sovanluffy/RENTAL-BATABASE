package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Exception.ConflictException;
import com.rental_api.rental.Exception.PropertyNotFoundException;
import com.rental_api.rental.Repository.PropertyRepository;
import com.rental_api.rental.Repository.ReviewRepository;
import com.rental_api.rental.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public ReviewResponse createReview(Long propertyId, ReviewRequest request, Authentication auth) {
        User user = (User) auth.getPrincipal(); // ✅ User entity from filter

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        // Check if this user already has a review
        if (reviewRepository.existsByUserAndProperty(user, property)) {
            throw new ConflictException("You already reviewed this property. Use update.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProperty(property);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        property.updateReviewStats();
        propertyRepository.save(property);

        return mapToResponse(review);
    }

    @Override
    public ReviewResponse updateReview(Long propertyId, ReviewRequest request, Authentication auth) {
        User user = (User) auth.getPrincipal();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        Review review = reviewRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new ConflictException("You haven't reviewed this property yet."));

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        property.updateReviewStats();
        propertyRepository.save(property);

        return mapToResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        return property.getReviews().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getUserReview(Long propertyId, Authentication auth) {
        User user = (User) auth.getPrincipal();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        Review review = reviewRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new ConflictException("You haven't reviewed this property yet."));

        return mapToResponse(review);
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProperty().getId(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
