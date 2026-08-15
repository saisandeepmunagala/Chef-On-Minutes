package com.chefonminutes.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/** Payload a chef sends to open up a new bookable time block. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotRequestDTO {
    @NotNull
    @Future
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
