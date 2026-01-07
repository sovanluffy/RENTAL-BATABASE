package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Entity.Review;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Exception.UnauthorizedException;
import com.rental_api.rental.Exception.UserNotFoundException;
import com.rental_api.rental.Repository.PropertyRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public PropertyResponse createProperty(PropertyRequest request, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean allowed = auth.getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_AGENT") ||
                        a.getAuthority().equals("ROLE_ADMIN")
                );

        if (!allowed) {
            throw new UnauthorizedException("Only AGENT or ADMIN can create properties");
        }

        Property property = new Property();
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setAddress(request.getAddress());
        property.setPrice(request.getPrice());
        property.setAgent(user);

        propertyRepository.save(property);

        return mapToResponse(property);
    }

    @Override
    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        return mapToResponse(property);
    }

    private PropertyResponse mapToResponse(Property property) {
        PropertyResponse response = new PropertyResponse();
        response.setId(property.getId());
        response.setTitle(property.getTitle());
        response.setDescription(property.getDescription());
        response.setAddress(property.getAddress());
        response.setPrice(property.getPrice());
        response.setUserId(property.getAgent().getId());
        response.setUsername(property.getAgent().getUsername());
        response.setCreatedAt(property.getCreatedAt());
        response.setUpdatedAt(property.getUpdatedAt());

        if (property.getReviews() != null) {
            response.setReviews(property.getReviews().stream()
                    .map(review -> {
                        PropertyResponse.ReviewDTO r = new PropertyResponse.ReviewDTO();
                        r.setId(review.getId());
                        r.setRating(review.getRating());
                        r.setComment(review.getComment());
                        r.setCreatedAt(review.getCreatedAt());
                        return r;
                    }).collect(Collectors.toList()));
        }

        response.setAverageRating(property.getAverageRating());
        response.setTotalReviews(property.getTotalReviews());

        return response;
    }
}
