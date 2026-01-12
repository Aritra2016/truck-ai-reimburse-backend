package com.aritra.truck_ai_reimburse.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    @NotBlank
    private String sender; // DRIVER / SYSTEM / BOT
    @NotBlank
    private String message;

    private String intent; // SUBMIT_EXPENSE, CHECK_PAY, RAISE_DISPUTE

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}
