package com.aritra.truck_ai_reimburse.DTOs;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayoutRequestDTO {

    @NotNull
    private Long driverId;

    @Positive
    private BigDecimal amount;

    private String currency; // USD / CAD

    private String payoutMethod; // ACH / RTP / CARD / EFT
}
