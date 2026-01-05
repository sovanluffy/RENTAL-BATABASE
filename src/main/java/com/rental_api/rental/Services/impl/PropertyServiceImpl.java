package com.rental_api.rental.Services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Entity.User;
import com.rental_api.rental.Exception.UnauthorizedException;
import com.rental_api.rental.Exception.UserNotFoundException;
import com.rental_api.rental.Repository.PropertyRepository;
import com.rental_api.rental.Repository.UserRepository;
import com.rental_api.rental.Services.PropertyService;

import jakarta.el.PropertyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    // ---------------- ROLE CHECK ----------------
    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
    }

    private boolean isAgent(User user) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals("AGENT"));
    }

    // ---------------- GET CURRENT USER ----------------
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    // ---------------- MAPPER ----------------
    private PropertyResponse map(Property p) {
        return PropertyResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .address(p.getAddress())
                .price(p.getPrice())
                .agentName(p.getAgent().getUsername())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    // ---------------- CREATE (AGENT ONLY) ----------------
    @Override
    public PropertyResponse create(PropertyRequest request) throws Exception {
        User user = getCurrentUser();

        if (!isAgent(user)) {
            throw new UnauthorizedException("Only AGENT can create property");
        }

        if (request.getPrice() == null || request.getPrice() <= 0) {
            throw new Exception("Price must be positive");
        }

        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .price(request.getPrice())
                .agent(user)
                .build();

        return map(propertyRepository.save(property));
    }

    // ---------------- GET ALL ----------------
    @Override
    public List<PropertyResponse> getAll() {
        User user = getCurrentUser();

        List<Property> properties;
        if (isAdmin(user)) {
            properties = propertyRepository.findAll();
        } else if (isAgent(user)) {
            properties = propertyRepository.findByAgentId(user.getId());
        } else {
            throw new UnauthorizedException("Access denied");
        }

        return properties.stream().map(this::map).toList();
    }

    // ---------------- GET BY ID ----------------
    @Override
    public PropertyResponse getById(Long propertyId) {
        User user = getCurrentUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + propertyId + " not found"));

        if (isAdmin(user) || property.getAgent().getId().equals(user.getId())) {
            return map(property);
        }

        throw new UnauthorizedException("Access denied");
    }

    // ---------------- UPDATE ----------------
    @Override
    public PropertyResponse update(Long propertyId, PropertyRequest request) throws Exception {
        User user = getCurrentUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + propertyId + " not found"));

        if (!isAdmin(user) && !property.getAgent().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        if (request.getPrice() == null || request.getPrice() <= 0) {
            throw new Exception("Price must be positive");
        }

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setAddress(request.getAddress());
        property.setPrice(request.getPrice());

        return map(propertyRepository.save(property));
    }

    // ---------------- DELETE ----------------
    @Override
    public void delete(Long propertyId) {
        User user = getCurrentUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + propertyId + " not found"));

        if (!isAdmin(user) && !property.getAgent().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        propertyRepository.delete(property);
    }

    @Override
    public <PropertyRequest> PropertyResponse create(Integer userId, PropertyRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public PropertyResponse create(Integer userId, PropertyRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public List<PropertyResponse> getAll(Integer userId) {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public PropertyResponse getById(Integer userId, Long propertyId) {
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public <PropertyRequest> PropertyResponse update(Integer userId, Long propertyId, PropertyRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public PropertyResponse update(Integer userId, Long propertyId, PropertyRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Integer userId, Long propertyId) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<PropertyResponse> getProperties(Integer userId) {
        throw new UnsupportedOperationException("Unimplemented method 'getProperties'");
    }
}
