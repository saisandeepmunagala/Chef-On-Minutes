package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A single "chef comes to home and cooks lunch" booking.
 * Session date/time and address are snapshotted at creation so later Slot/Address edits
 * never change the historical record.
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private User chef;

    /** Operational link to the current slot reservation; not the source of truth for historical display. */
    @OneToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String addressSnapshot;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private Double totalAmount;

    @Version
    private Long version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingItem> items = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addItem(BookingItem item) {
        items.add(item);
        item.setBooking(this);
    }
}
