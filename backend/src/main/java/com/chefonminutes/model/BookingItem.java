package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One dish line item within a Booking; snapshots dish name and price so later
 * ChefDish edits or removals never change historical totals.
 */
@Entity
@Table(name = "booking_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "chef_dish_id")
    private ChefDish chefDish;

    private String dishName;

    private Integer quantity;

    private Double priceAtBooking;

    public Double lineTotal() {
        return priceAtBooking * quantity;
    }
}
