package com.aritra.truck_ai_reimburse.DTOs;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseConfirmationDTO {

    private Long expenseBillId;
    private boolean confirmed;
}
