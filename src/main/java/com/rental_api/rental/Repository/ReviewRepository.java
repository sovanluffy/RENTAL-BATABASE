package com.rental_api.rental.Repository;

import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserAndProperty(User user, Property property);
}
