package com.chefonminutes.model;

/**
 * Lifecycle states for a Booking. Allowed transitions are enforced in BookingServiceImpl.
 */
public enum BookingStatus {
    PENDING,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
