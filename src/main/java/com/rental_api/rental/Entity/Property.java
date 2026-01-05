package com.rental_api.rental.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(nullable = false)
     private String title;

     @Column(columnDefinition = "TEXT")
     private String description;

     @Column(nullable = false)
     private String address;

     @Column(nullable = false)
     private Float price;

     @ManyToOne
     @JoinColumn(name = "agent_id", nullable = false)
     private User agent;
     
     private LocalDateTime createdAt;
     private LocalDateTime updatedAt;

     @PrePersist
     void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
     }

        @PreUpdate
        void onUpdate(){
            updatedAt = LocalDateTime.now();
        }

}
