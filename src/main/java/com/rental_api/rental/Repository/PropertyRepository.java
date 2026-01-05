package com.rental_api.rental.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rental_api.rental.Entity.Property;
import java.util.List;


public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByAgentId(Long agentId);
}
