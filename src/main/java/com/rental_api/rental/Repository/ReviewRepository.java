package com.rental_api.rental.Repository;

import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Check if user already reviewed the property
    boolean existsByUserAndProperty(User user, Property property);

    // Get review by user and property
    Optional<Review> findByUserAndProperty(User user, Property property);

    // Get all reviews for a property
    List<Review> findAllByProperty(Property property);
}
