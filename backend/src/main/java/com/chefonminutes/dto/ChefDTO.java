package com.chefonminutes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Shape returned to the frontend for chef listings/profile pages.
 * Keeps entity details decoupled from the API contract.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefDTO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String bio;
    private String specialty;
    private Boolean available;
    private Double ratingAvg;
    private Integer ratingCount;
    private List<ChefDishDTO> menu;
}

