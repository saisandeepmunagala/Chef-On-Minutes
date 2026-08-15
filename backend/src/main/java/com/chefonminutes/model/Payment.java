package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A payment record for a Booking. Amount must equal Booking.totalAmount (enforced in PaymentService).
 */
@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(name = "uk_payments_booking", columnNames = "booking_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Double amount;

    private Double refundedAmount;
    private LocalDateTime refundedAt;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionRef;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
