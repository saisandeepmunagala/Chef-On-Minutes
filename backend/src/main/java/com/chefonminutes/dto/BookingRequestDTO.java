package com.chefonminutes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Payload sent by the frontend when a customer books a chef for a session.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {

    @NotNull
    private Long customerId;

    @NotNull
    private Long chefId;

    @NotNull
    private Long slotId;

    private Long addressId;

    @NotEmpty
    @Valid
    private List<BookingItemRequestDTO> items;
}

