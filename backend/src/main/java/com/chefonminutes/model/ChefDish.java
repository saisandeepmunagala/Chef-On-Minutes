package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A chef's own menu entry: their price for a catalog Dish. Chef-managed (add/update/soft-delete).
 */
@Entity
@Table(name = "chef_dishes", uniqueConstraints = @UniqueConstraint(
        name = "uk_chef_dishes_chef_dish", columnNames = {"chef_profile_id", "dish_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chef_profile_id")
    private ChefProfile chefProfile;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Double pricePerUnit;

    @Builder.Default
    private Boolean available = true;

    @Builder.Default
    private Boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (available == null) available = true;
        if (active == null) active = true;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
