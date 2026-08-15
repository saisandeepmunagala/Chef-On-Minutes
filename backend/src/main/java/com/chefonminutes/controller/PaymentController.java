package com.chefonminutes.controller;

import com.chefonminutes.dto.PaymentDTO;
import com.chefonminutes.dto.PaymentRequestDTO;
import com.chefonminutes.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> pay(@Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.pay(request));
    }

    @PostMapping("/{bookingId}/refund")
    public ResponseEntity<PaymentDTO> refund(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.refund(bookingId));
    }
}
