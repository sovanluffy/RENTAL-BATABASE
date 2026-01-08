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

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    // ================= CREATE REVIEW =================
    @Override
    public ReviewResponse createReview(ReviewRequest request, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        if (reviewRepository.existsByUserAndProperty(user, property)) {
            throw new ConflictException(
                    "You have already reviewed this property. Use update to change it."
            );
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

    // ================= UPDATE REVIEW =================
    @Override
    public ReviewResponse updateReview(Long propertyId, ReviewRequest request, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        Review review = reviewRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new ConflictException(
                        "You have not reviewed this property yet."
                ));

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        property.updateReviewStats();
        propertyRepository.save(property);

        return mapToResponse(review);
    }

    // ================= GET REVIEWS BY PROPERTY =================
    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProperty(Long propertyId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        return property.getReviews()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET USER REVIEW =================
    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getUserReview(Long propertyId, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        Review review = reviewRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new ConflictException("You have not reviewed this property yet."));

        return mapToResponse(review);
    }

    // ================= MAPPER =================
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
