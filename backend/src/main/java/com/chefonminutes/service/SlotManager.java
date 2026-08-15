package com.chefonminutes.service;

import com.chefonminutes.dto.SlotDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Domain service that owns Slot allocation/release, independent of Booking orchestration.
 */
public interface SlotManager {

    SlotDTO createSlot(Long chefUserId, LocalDate date, java.time.LocalTime startTime, java.time.LocalTime endTime);

    List<SlotDTO> getAvailableSlots(Long chefProfileId, LocalDate date);

    List<SlotDTO> getSlotsForChef(Long chefProfileId);

    /** Reserves an AVAILABLE slot for the given booking; throws if not available (optimistic-lock safe). */
    void reserveSlot(Long slotId, Long bookingId);

    /** Flips a BOOKED slot back to AVAILABLE, e.g. after cancellation. */
    void releaseSlot(Long slotId);

    boolean isSlotAvailable(Long slotId);
}
