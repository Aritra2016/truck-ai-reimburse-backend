package com.aritra.truck_ai_reimburse.DTOs;

import com.aritra.truck_ai_reimburse.enums.PodStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PodUploadResponseDTO {

    private String sessionId;
    private String tripId;
    private PodStatus podStatus;
    private String deliveryDate;
    private String deliveryLocation;
    private String message;

}
