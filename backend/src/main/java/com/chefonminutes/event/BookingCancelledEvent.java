package com.chefonminutes.event;

/** Published when a Booking is cancelled; listeners release the held Slot (and handle refunds). */
public class BookingCancelledEvent {
    private final Long bookingId;
    private final Long slotId;

    public BookingCancelledEvent(Long bookingId, Long slotId) {
        this.bookingId = bookingId;
        this.slotId = slotId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getSlotId() {
        return slotId;
    }
}
