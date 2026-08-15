package com.chefonminutes.service.impl;

import com.chefonminutes.event.BookingCancelledEvent;
import com.chefonminutes.service.SlotManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Decoupled from BookingServiceImpl: releases the slot whenever a booking is cancelled. */
@Component
@RequiredArgsConstructor
public class SlotReleaseListener {

    private final SlotManager slotManager;

    @EventListener
    public void onBookingCancelled(BookingCancelledEvent event) {
        if (event.getSlotId() != null) {
            slotManager.releaseSlot(event.getSlotId());
        }
    }
}
