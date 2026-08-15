package com.chefonminutes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A saved address for a user; soft-deleted so past bookings keep a valid reference.
 */
@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String line1;
    private String line2;
    private String city;
    private String state;
    private String pincode;

    @Enumerated(EnumType.STRING)
    private AddressLabel label;

    private Boolean isDefault;

    @Builder.Default
    private Boolean active = true;

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (active == null) {
            active = true;
        }
    }

    public String toSnapshotText() {
        return String.join(", ",
                line1 != null ? line1 : "",
                line2 != null ? line2 : "",
                city != null ? city : "",
                state != null ? state : "",
                pincode != null ? pincode : "");
    }
}
