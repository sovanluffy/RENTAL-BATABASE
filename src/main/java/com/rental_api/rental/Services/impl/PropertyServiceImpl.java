package com.rental_api.rental.Services.impl;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Exception.UnauthorizedException;
import com.rental_api.rental.Exception.UserNotFoundException;
import com.rental_api.rental.Repository.PropertyRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    // ================= CREATE =================
    @Override
    public PropertyResponse createProperty(PropertyRequest request, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Only AGENT or ADMIN
        boolean allowed = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_ADMIN"));
        if (!allowed) throw new UnauthorizedException("Only AGENT or ADMIN can create properties");

        // Validate images
        validateImages(request.getImages());

        Property property = new Property();
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setAddress(request.getAddress());
        property.setPrice(request.getPrice());
        property.setAgent(user);
        property.setImages(request.getImages());

        propertyRepository.save(property);
        return mapToResponse(property);
    }

    // ================= UPDATE =================
    @Override
    public PropertyResponse updateProperty(Long id, PropertyRequest request, Authentication auth) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        boolean allowed = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_AGENT") || a.getAuthority().equals("ROLE_ADMIN"));
        if (!allowed) throw new UnauthorizedException("Only AGENT or ADMIN can update property");

        // Validate images
        validateImages(request.getImages());

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setAddress(request.getAddress());
        property.setPrice(request.getPrice());
        property.setImages(request.getImages());

        propertyRepository.save(property);
        return mapToResponse(property);
    }

    // ================= GET SINGLE =================
    @Override
    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        return mapToResponse(property);
    }

    // ================= GET ALL =================
    @Override
    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= DELETE =================
    @Override
    public void deleteProperty(Long id, Authentication auth) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        boolean allowed = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_AGENT"));
        if (!allowed) throw new UnauthorizedException("Only ADMIN or AGENT can delete property");

        propertyRepository.delete(property);
    }

    // ================= IMAGE VALIDATION =================
    private void validateImages(List<String> images) {
        if (images == null) return; // No images → ok

        for (String img : images) {
            try {
                new URL(img); // Throws if not valid URL
            } catch (MalformedURLException e) {
                throw new ValidationException("Invalid image URL: " + img);
            }
        }
    }

    // ================= MAPPER =================
    private PropertyResponse mapToResponse(Property property) {
        PropertyResponse res = new PropertyResponse();
        res.setId(property.getId());
        res.setTitle(property.getTitle());
        res.setDescription(property.getDescription());
        res.setAddress(property.getAddress());
        res.setPrice(property.getPrice());

        res.setUserId(property.getAgent().getId());
        res.setUsername(property.getAgent().getUsername());

        res.setTotalReviews(property.getTotalReviews());
        res.setAvgRating(property.getAvgRating());

        res.setImages(property.getImages());

        res.setCreatedAt(property.getCreatedAt());
        res.setUpdatedAt(property.getUpdatedAt());

        return res;
    }
}
