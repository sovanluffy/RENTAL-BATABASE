package com.rental_api.rental.Services;

import org.springframework.security.core.Authentication;
import java.util.List;
import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;

public interface PropertyService {

    PropertyResponse createProperty(PropertyRequest request, Authentication auth);

    PropertyResponse updateProperty(Long id, PropertyRequest request, Authentication auth);

    // ✅ GET ALL
    List<PropertyResponse> getAllProperties();

    // ✅ DELETE
    void deleteProperty(Long id, Authentication auth);
}
