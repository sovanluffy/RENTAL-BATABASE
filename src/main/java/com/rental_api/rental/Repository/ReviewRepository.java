package com.rental_api.rental.Repository;

import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserAndProperty(User user, Property property);
}
