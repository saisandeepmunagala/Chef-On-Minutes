package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Chef-only data, kept separate from User so the account table stays role-agnostic.
 */
@Entity
@Table(name = "chef_profiles", uniqueConstraints = @UniqueConstraint(name = "uk_chef_profiles_user", columnNames = "user_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String bio;

    private String specialty;

    private Boolean available;

    /** Aggregate cache, recomputed by the ReviewSubmittedEvent listener - do not set directly elsewhere. */
    @Builder.Default
    private Double ratingAvg = 0.0;

    @Builder.Default
    private Integer ratingCount = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (available == null) {
            available = true;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
