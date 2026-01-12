package com.aritra.truck_ai_reimburse.DTOs;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisputeRequestDTO {

    @NotNull
    private Long driverId;

    @NotBlank
    private String disputeType; // PAY / EXPENSE / DETENTION

    @NotBlank
    private String reason;

    private Long tripId;
}
