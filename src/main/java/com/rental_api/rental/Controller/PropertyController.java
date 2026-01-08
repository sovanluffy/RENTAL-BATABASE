package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.ApiResponse;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import com.rental_api.rental.Services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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


    // ================= UPDATE PROPERTY =================
     @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyRequest request,
            Authentication auth
    ) {
        PropertyResponse response = propertyService.updateProperty(id, request, auth);
        return ResponseEntity.ok(response);
    }
}
