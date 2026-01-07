package com.rental_api.rental.Services;

import org.springframework.security.core.Authentication;
import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;

import java.util.List;

public interface PropertyService {

    // Create new property
    PropertyResponse createProperty(PropertyRequest request, Authentication auth);

    // Get all properties
    List<PropertyResponse> getAllProperties();

    // Get property by id
    PropertyResponse getPropertyById(Long id);
}
