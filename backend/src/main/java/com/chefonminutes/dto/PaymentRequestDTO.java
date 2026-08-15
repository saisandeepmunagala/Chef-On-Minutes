package com.chefonminutes.dto;

import com.chefonminutes.model.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    @NotNull
    private Long bookingId;

    @NotNull
    private PaymentMode mode;
}
