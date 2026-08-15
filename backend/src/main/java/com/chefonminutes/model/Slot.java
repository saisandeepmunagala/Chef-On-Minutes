package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A time block a chef has made available. bookingId is a plain snapshot column,
 * not a JPA relation, to keep Booking<->Slot decoupled after release/cancellation.
 */
@Entity
@Table(name = "slots", uniqueConstraints = @UniqueConstraint(
        name = "uk_slots_chef_date_start", columnNames = {"chef_profile_id", "date", "start_time"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chef_profile_id")
    private ChefProfile chefProfile;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    /** Nullable back-reference to the booking currently holding this slot. */
    private Long bookingId;

    @Version
    private Long version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) {
            status = SlotStatus.AVAILABLE;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
