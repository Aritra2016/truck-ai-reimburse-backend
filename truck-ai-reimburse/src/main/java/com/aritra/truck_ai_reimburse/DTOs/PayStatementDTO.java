package com.aritra.truck_ai_reimburse.DTOs;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PayStatementDTO {

    private String statementNumber;

    private Long driverId;
    private Long tripId;

    private BigDecimal grossPay;
    private BigDecimal deductions;
    private BigDecimal netPay;

    private LocalDateTime generatedAt;
}
