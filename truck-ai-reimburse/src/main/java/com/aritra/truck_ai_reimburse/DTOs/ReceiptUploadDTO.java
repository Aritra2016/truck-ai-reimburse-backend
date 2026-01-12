package com.aritra.truck_ai_reimburse.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptUploadDTO {

    @NotNull
    private Long expenseId;

    private String fileName;
    private String fileUrl;

    private Double confidenceScore;
}
