package com.aritra.truck_ai_reimburse.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {

    private String sessionId;   // to track conversation
    private String message;     // user message text
    private String userId;
}
