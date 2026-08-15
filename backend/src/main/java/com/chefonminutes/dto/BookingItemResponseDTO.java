package com.chefonminutes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingItemResponseDTO {
    private String dishName;
    private Integer quantity;
    private Double priceAtBooking;
    private Double lineTotal;
}
