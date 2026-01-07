package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.ApiResponse;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import com.rental_api.rental.Services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    // ================= CREATE PROPERTY =================
    @PostMapping
    public ResponseEntity<ApiResponse<PropertyResponse>> createProperty(
            @RequestBody PropertyRequest request,
            Authentication auth
    ) {
        PropertyResponse res = propertyService.createProperty(request, auth);
        return ResponseEntity.status(201)
                .body(ApiResponse.success(201, "Property created successfully", res));
    }

    // ================= GET ALL PROPERTIES =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getAllProperties() {
        List<PropertyResponse> list = propertyService.getAllProperties();
        return ResponseEntity.ok(ApiResponse.success(200, "All properties fetched", list));
    }

    // ================= GET PROPERTY BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyResponse>> getPropertyById(@PathVariable Long id) {
        PropertyResponse res = propertyService.getPropertyById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Property fetched successfully", res));
    }
}
