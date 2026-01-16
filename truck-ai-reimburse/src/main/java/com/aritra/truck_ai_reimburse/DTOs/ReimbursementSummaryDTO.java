package com.aritra.truck_ai_reimburse.DTOs;

import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReimbursementSummaryDTO {

    private String sessionId;
    private String tripId;
    private BigDecimal totalAmountInINR;
    private ReimbursementStatus status;
    private String message;
}
