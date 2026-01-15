package com.aritra.truck_ai_reimburse.DTOs;

import com.aritra.truck_ai_reimburse.enums.TripStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponseDTO {
    private Long tripId;
    private String tripNumber;
    private String origin;
    private String destination;
    private TripStatus status;
    private BigDecimal totalAmount;
}
