package com.chefonminutes.service.impl;

import com.chefonminutes.dto.SlotDTO;
import com.chefonminutes.exception.InvalidStateException;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.ChefProfile;
import com.chefonminutes.model.Slot;
import com.chefonminutes.model.SlotStatus;
import com.chefonminutes.repository.ChefProfileRepository;
import com.chefonminutes.repository.SlotRepository;
import com.chefonminutes.service.SlotManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotManagerImpl implements SlotManager {

    private final SlotRepository slotRepository;
    private final ChefProfileRepository chefProfileRepository;

    @Override
    public SlotDTO createSlot(Long chefUserId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        ChefProfile chefProfile = chefProfileRepository.findByUserId(chefUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef profile not found for user " + chefUserId));
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
        Slot slot = Slot.builder()
                .chefProfile(chefProfile)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .status(SlotStatus.AVAILABLE)
                .build();
        return toDTO(slotRepository.save(slot));
    }

    @Override
    public List<SlotDTO> getAvailableSlots(Long chefProfileId, LocalDate date) {
        return slotRepository.findByChefProfileIdAndDateAndStatus(chefProfileId, date, SlotStatus.AVAILABLE)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<SlotDTO> getSlotsForChef(Long chefProfileId) {
        return slotRepository.findByChefProfileId(chefProfileId).stream().map(this::toDTO).toList();
    }

    @Override
    public void reserveSlot(Long slotId, Long bookingId) {
        Slot slot = getSlotOrThrow(slotId);
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new InvalidStateException("Slot " + slotId + " is not available");
        }
        slot.setStatus(SlotStatus.BOOKED);
        slot.setBookingId(bookingId);
        slotRepository.save(slot);
    }

    @Override
    public void releaseSlot(Long slotId) {
        Slot slot = getSlotOrThrow(slotId);
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setBookingId(null);
        slotRepository.save(slot);
    }

    @Override
    public boolean isSlotAvailable(Long slotId) {
        return getSlotOrThrow(slotId).getStatus() == SlotStatus.AVAILABLE;
    }

    private Slot getSlotOrThrow(Long slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + slotId));
    }

    private SlotDTO toDTO(Slot slot) {
        return SlotDTO.builder()
                .id(slot.getId())
                .chefProfileId(slot.getChefProfile().getId())
                .date(slot.getDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .status(slot.getStatus())
                .build();
    }
}
