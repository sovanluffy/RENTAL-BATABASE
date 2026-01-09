package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.ApiResponse;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import com.rental_api.rental.Services.PropertyService;
import jakarta.validation.Valid;
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

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<PropertyResponse>> createProperty(
            @Valid @RequestBody PropertyRequest request,
            Authentication auth
    ) {
        PropertyResponse res = propertyService.createProperty(request, auth);
        return ResponseEntity.status(201)
                .body(ApiResponse.success(201, "Property created successfully", res));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyResponse>> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest request,
            Authentication auth
    ) {
        PropertyResponse res = propertyService.updateProperty(id, request, auth);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Property updated successfully", res)
        );
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getAllProperties() {
        List<PropertyResponse> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(
                ApiResponse.success(200, "Get all properties successfully", properties)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProperty(
            @PathVariable Long id,
            Authentication auth
    ) {
        propertyService.deleteProperty(id, auth);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Property deleted successfully", null)
        );
    }

    // ================= GET SINGLE PROPERTY =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyResponse>> getPropertyById(
            @PathVariable Long id
    ) {
        PropertyResponse res = propertyService.getPropertyById(id);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Property fetched successfully", res)
        );
    }
}
