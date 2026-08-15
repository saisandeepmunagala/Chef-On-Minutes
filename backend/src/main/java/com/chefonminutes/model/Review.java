package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A customer's rating for a chef, allowed only after the Booking reaches COMPLETED.
 * customerId/chefId are denormalized from the booking for query convenience.
 */
@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(name = "uk_reviews_booking", columnNames = "booking_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private User chef;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
