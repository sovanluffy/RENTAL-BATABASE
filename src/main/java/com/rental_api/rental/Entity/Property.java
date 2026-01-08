package com.rental_api.rental.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "avg_rating")
    private Double avgRating = 0.0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        updateReviewStats();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateReviewStats();
    }

    /**
     * Updates totalReviews and avgRating based on current reviews list
     */
    public void updateReviewStats() {
        if (reviews == null || reviews.isEmpty()) {
            totalReviews = 0;
            avgRating = 0.0;
        } else {
            totalReviews = reviews.size();
            avgRating = reviews.stream()
                               .mapToDouble(Review::getRating)
                               .average()
                               .orElse(0.0);
        }
    }
}
