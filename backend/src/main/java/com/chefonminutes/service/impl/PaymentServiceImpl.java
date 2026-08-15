package com.chefonminutes.service.impl;

import com.chefonminutes.dto.PaymentDTO;
import com.chefonminutes.dto.PaymentRequestDTO;
import com.chefonminutes.exception.InvalidStateException;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.Booking;
import com.chefonminutes.model.Payment;
import com.chefonminutes.model.PaymentStatus;
import com.chefonminutes.repository.BookingRepository;
import com.chefonminutes.repository.PaymentRepository;
import com.chefonminutes.service.BookingService;
import com.chefonminutes.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Override
    public PaymentDTO pay(PaymentRequestDTO request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));
        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new InvalidStateException("Booking " + booking.getId() + " is already paid");
        }

        // No real payment gateway yet - simulate a successful transaction.
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .mode(request.getMode())
                .status(PaymentStatus.SUCCESS)
                .transactionRef(UUID.randomUUID().toString())
                .paidAt(LocalDateTime.now())
                .build();
        payment = paymentRepository.save(payment);

        bookingService.confirmBooking(booking.getId());

        return toDTO(payment);
    }

    @Override
    public PaymentDTO refund(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for booking " + bookingId));
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidStateException("Payment for booking " + bookingId + " is not refundable from status " + payment.getStatus());
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAmount(payment.getAmount());
        payment.setRefundedAt(LocalDateTime.now());
        return toDTO(paymentRepository.save(payment));
    }

    private PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .refundedAmount(payment.getRefundedAmount())
                .mode(payment.getMode())
                .status(payment.getStatus())
                .transactionRef(payment.getTransactionRef())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
