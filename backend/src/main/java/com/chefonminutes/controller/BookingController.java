package com.chefonminutes.controller;

import com.chefonminutes.dto.BookingRequestDTO;
import com.chefonminutes.dto.BookingResponseDTO;
import com.chefonminutes.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints for booking a chef's home lunch session.
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsForCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getBookingsForCustomer(customerId));
    }

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsForChef(@PathVariable Long chefId) {
        return ResponseEntity.ok(bookingService.getBookingsForChef(chefId));
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    @PatchMapping("/{bookingId}/start")
    public ResponseEntity<BookingResponseDTO> startBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.startBooking(bookingId));
    }

    @PatchMapping("/{bookingId}/complete")
    public ResponseEntity<BookingResponseDTO> completeBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.completeBooking(bookingId));
    }
}
