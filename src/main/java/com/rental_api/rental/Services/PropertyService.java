package com.rental_api.rental.Services;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PropertyService {

    PropertyResponse createProperty(PropertyRequest request, Authentication auth);

    PropertyResponse updateProperty(Long id, PropertyRequest request, Authentication auth);

    PropertyResponse getPropertyById(Long id);

    List<PropertyResponse> getAllProperties();

    void deleteProperty(Long id, Authentication auth);
}
