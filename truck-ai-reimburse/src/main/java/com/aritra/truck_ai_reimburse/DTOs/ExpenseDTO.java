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
public class ExpenseDTO {

    private Long expenseId;

    @NotNull
    private Long tripId;

    @NotNull
    private String expenseType; // FUEL, TOLL, LUMPER

    @Positive
    private BigDecimal amount;

    private String currency;

    private LocalDateTime expenseDate;

    private boolean approved;
}
