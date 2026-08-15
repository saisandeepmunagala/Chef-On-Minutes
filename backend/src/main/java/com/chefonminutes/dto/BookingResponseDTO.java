package com.chefonminutes.dto;

import com.chefonminutes.model.BookingStatus;
import com.chefonminutes.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Shape returned to the frontend after a booking is created/fetched.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long id;
    private String customerName;
    private String chefName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String addressSnapshot;
    private BookingStatus status;
    private Double totalAmount;
    private PaymentStatus paymentStatus;
    private List<BookingItemResponseDTO> items;
}

