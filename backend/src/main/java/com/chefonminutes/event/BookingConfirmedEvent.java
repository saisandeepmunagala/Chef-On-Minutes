package com.chefonminutes.event;

/** Published when payment succeeds and a Booking flips to CONFIRMED; extension point for notifications. */
public class BookingConfirmedEvent {
    private final Long bookingId;

    public BookingConfirmedEvent(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingId() {
        return bookingId;
    }
}
