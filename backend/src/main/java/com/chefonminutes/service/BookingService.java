package com.chefonminutes.service;

import com.chefonminutes.dto.BookingRequestDTO;
import com.chefonminutes.dto.BookingResponseDTO;

import java.util.List;

/**
 * Business logic contract for booking a chef's lunch session.
 */
public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request);

    List<BookingResponseDTO> getBookingsForCustomer(Long customerId);

    List<BookingResponseDTO> getBookingsForChef(Long chefId);

    BookingResponseDTO cancelBooking(Long bookingId);

    BookingResponseDTO confirmBooking(Long bookingId);

    BookingResponseDTO startBooking(Long bookingId);

    BookingResponseDTO completeBooking(Long bookingId);
}

