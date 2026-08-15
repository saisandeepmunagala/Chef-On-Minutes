package com.chefonminutes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Payload a chef sends to add or update their own menu entry for a catalog Dish. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefDishRequestDTO {
    @NotNull
    private Long dishId;

    @NotNull
    private Double pricePerUnit;

    private Boolean available;
}
