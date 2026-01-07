package com.rental_api.rental.Services;

import org.springframework.security.core.Authentication;
import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;

public interface PropertyService {
    PropertyResponse createProperty(PropertyRequest request, Authentication auth);
}
