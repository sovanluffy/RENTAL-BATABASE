package com.rental_api.rental.Controller;

import com.rental_api.rental.Dtos.Request.PropertyRequest;
import com.rental_api.rental.Dtos.Response.PropertyResponse;
import com.rental_api.rental.Entity.Property;
import com.rental_api.rental.Services.PropertyService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<PropertyResponse> create(@RequestBody PropertyRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.create(request));
    }

    @GetMapping
    public List<PropertyResponse> getAll() {
        return propertyService.getAll();
    }

    @GetMapping("/{id}")
    public PropertyResponse getById(@PathVariable Long id) {
        return propertyService.getById(id);
    }

    @PutMapping("/{id}")
    public PropertyResponse update(@PathVariable Long id, @RequestBody PropertyRequest request) throws Exception {
        return propertyService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}