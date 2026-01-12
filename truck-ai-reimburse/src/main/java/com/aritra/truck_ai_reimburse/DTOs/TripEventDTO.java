package com.aritra.truck_ai_reimburse.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TripEventDTO {

    @NotNull
    private Long tripId;

    @NotBlank
    private String eventType; // TRIP_STARTED / TRIP_COMPLETED

    private LocalDateTime eventTime;

    private String source; // TMS / ELD / MANUAL
}
