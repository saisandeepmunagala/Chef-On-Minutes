package com.chefonminutes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long bookingId;
    private Long customerId;
    private Long chefId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
