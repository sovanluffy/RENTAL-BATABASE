package com.rental_api.rental.Dtos.Response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {

    private Long id;
    private String title;
    private String description;
    private String address;
    private Float price;
    private String agentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
