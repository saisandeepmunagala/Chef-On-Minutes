package com.chefonminutes.service;

import com.chefonminutes.dto.PaymentDTO;
import com.chefonminutes.dto.PaymentRequestDTO;

public interface PaymentService {
    /** Simulates a successful payment (no real gateway yet), then confirms the Booking. */
    PaymentDTO pay(PaymentRequestDTO request);

    PaymentDTO refund(Long bookingId);
}
