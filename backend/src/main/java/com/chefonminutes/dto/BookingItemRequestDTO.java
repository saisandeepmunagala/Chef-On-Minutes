package com.chefonminutes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** One requested dish + quantity within a BookingRequestDTO. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingItemRequestDTO {
    @NotNull
    private Long chefDishId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
