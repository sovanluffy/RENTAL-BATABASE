package com.rental_api.rental.Repository;

import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}
