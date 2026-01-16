package com.aritra.truck_ai_reimburse.DTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequestDTO {

    private String sessionId;
    private String userId;
    private String message;

}
