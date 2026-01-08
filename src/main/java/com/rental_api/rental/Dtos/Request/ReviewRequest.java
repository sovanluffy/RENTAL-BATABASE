package com.rental_api.rental.Dtos.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long propertyId;
    private Integer rating; // must be 1-5
    private String comment;
}
