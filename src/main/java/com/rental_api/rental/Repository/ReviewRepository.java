package com.rental_api.rental.Repository;

import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {  

    // Find all reviews by a specific user
    List<Review> findByUser(User user);

    // Optional: find review by user and rating
    List<Review> findByUserAndRating(User user, Integer rating);
}
