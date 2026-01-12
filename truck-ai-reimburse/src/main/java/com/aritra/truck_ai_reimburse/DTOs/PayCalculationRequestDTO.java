package com.aritra.truck_ai_reimburse.DTOs;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayCalculationRequestDTO {

    @NotNull
    private Long tripId;

    @NotNull
    private Long driverId;

    private boolean includeExpenses;
}
