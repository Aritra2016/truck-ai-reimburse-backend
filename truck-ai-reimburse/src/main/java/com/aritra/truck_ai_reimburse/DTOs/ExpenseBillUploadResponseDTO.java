package com.aritra.truck_ai_reimburse.DTOs;


import com.aritra.truck_ai_reimburse.enums.ExpenseCategory;
import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseBillUploadResponseDTO {

    private Long expenseBillId;

    private ExpenseCategory category;

    private BigDecimal amount;

    private String currency;

    private BigDecimal amountInINR;

    private String message;



}
