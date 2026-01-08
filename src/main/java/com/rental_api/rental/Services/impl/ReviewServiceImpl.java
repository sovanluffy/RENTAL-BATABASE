package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.ReviewRequest;
import com.rental_api.rental.Dtos.Response.ReviewResponse;
import com.rental_api.rental.Entity.*;
import com.rental_api.rental.Exception.*;
import com.rental_api.rental.Repository.*;
import com.rental_api.rental.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse createReview(ReviewRequest request, Authentication auth) {

        //  Get logged-in user
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        //  Get property
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        //  Validate rating
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        // Check for duplicate review
        if (reviewRepository.existsByUserAndProperty(user, property)) {
            throw new ConflictException("You have already reviewed this property. Reviews are final.");
        }

        // ✅ Create review
        Review review = new Review();
        review.setUser(user);
        review.setProperty(property);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        // 📊 Update property stats
        property.updateReviewStats();
        propertyRepository.save(property);

        return new ReviewResponse(
                review.getId(),
                property.getId(),
                user.getId(),
                user.getUsername(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
