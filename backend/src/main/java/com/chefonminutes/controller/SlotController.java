package com.chefonminutes.controller;

import com.chefonminutes.dto.SlotDTO;
import com.chefonminutes.dto.SlotRequestDTO;
import com.chefonminutes.service.SlotManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SlotController {

    private final SlotManager slotManager;

    @GetMapping("/api/chefs/{chefProfileId}/slots")
    public ResponseEntity<List<SlotDTO>> getAvailableSlots(
            @PathVariable Long chefProfileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(slotManager.getAvailableSlots(chefProfileId, date));
    }

    @GetMapping("/api/chefs/{chefProfileId}/slots/all")
    public ResponseEntity<List<SlotDTO>> getAllSlots(@PathVariable Long chefProfileId) {
        return ResponseEntity.ok(slotManager.getSlotsForChef(chefProfileId));
    }

    // TODO: chefUserId should come from the authenticated principal once JWT/session auth is implemented
    @PostMapping("/api/chefs/me/slots")
    public ResponseEntity<SlotDTO> createSlot(@RequestParam Long chefUserId, @Valid @RequestBody SlotRequestDTO request) {
        return ResponseEntity.ok(slotManager.createSlot(chefUserId, request.getDate(), request.getStartTime(), request.getEndTime()));
    }
}
