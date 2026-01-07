package com.rental_api.rental.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rental_api.rental.Entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
