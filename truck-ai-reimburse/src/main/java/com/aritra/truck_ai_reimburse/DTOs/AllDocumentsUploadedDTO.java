package com.aritra.truck_ai_reimburse.DTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllDocumentsUploadedDTO {

    private String sessionId;
    private String tripId;
    private String userId;

}
