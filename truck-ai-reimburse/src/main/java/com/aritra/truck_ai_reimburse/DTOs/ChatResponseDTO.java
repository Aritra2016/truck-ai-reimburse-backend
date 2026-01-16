package com.aritra.truck_ai_reimburse.DTOs;

import com.aritra.truck_ai_reimburse.enums.ChatIntent;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponseDTO {

    private String sessionId;
    private String replyMessage;
    private ChatIntent nextAction;
}
