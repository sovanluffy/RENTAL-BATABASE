package com.rental_api.rental.Services;

import com.rental_api.rental.Dtos.Response.PropertyResponse;
import java.util.List;

public interface PropertyService {

    <PropertyRequest> PropertyResponse create(Integer userId, PropertyRequest request);

    List<PropertyResponse> getAll(Integer userId);

    PropertyResponse getById(Integer userId, Long propertyId);

    <PropertyRequest> PropertyResponse update(Integer userId, Long propertyId, PropertyRequest request);

    void delete(Integer userId, Long propertyId);

    List<PropertyResponse> getProperties(Integer userId);

    PropertyResponse update(Integer userId, Long propertyId,
            com.rental_api.rental.Dtos.Request.PropertyRequest request);

    PropertyResponse create(Integer userId, com.rental_api.rental.Dtos.Request.PropertyRequest request);

    PropertyResponse create(com.rental_api.rental.Dtos.Request.PropertyRequest request) throws Exception;

    List<PropertyResponse> getAll();

    PropertyResponse getById(Long propertyId);

    PropertyResponse update(Long propertyId, com.rental_api.rental.Dtos.Request.PropertyRequest request) throws Exception;

    void delete(Long propertyId);
}
