package com.chefonminutes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.chefonminutes.model.PaymentMode;
import com.chefonminutes.model.PaymentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private Long bookingId;
    private Double amount;
    private Double refundedAmount;
    private PaymentMode mode;
    private PaymentStatus status;
    private String transactionRef;
    private LocalDateTime paidAt;
}
