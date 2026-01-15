package com.aritra.truck_ai_reimburse.DTOs;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTripRequestDTO {

    private String tripNumber;
    private Long driverId;
    private String origin;
    private String destination;
    private LocalDateTime pickupTime;
    private BigDecimal totalAmount;
}
