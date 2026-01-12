package com.aritra.truck_ai_reimburse.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatResponse {

    private String sessionId;
    private String reply;        // chatbot message
    private String nextAction;
}
