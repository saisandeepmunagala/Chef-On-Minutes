package com.chefonminutes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefDishDTO {
    private Long id;
    private Long dishId;
    private String dishName;
    private Double pricePerUnit;
    private Boolean available;
}
