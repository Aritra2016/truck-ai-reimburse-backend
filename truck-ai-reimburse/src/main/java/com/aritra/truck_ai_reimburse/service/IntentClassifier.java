package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.enums.ChatIntent;
import org.springframework.stereotype.Service;

@Service
public class IntentClassifier {

    public ChatIntent detectIntent(String message) {
        String text = message.toLowerCase();

        if (text.contains("pay")) {
            return ChatIntent.CHECK_PAY;
        }
        if (text.contains("trip is done") || text.contains("trip completed")) {
            return ChatIntent.TRIP_COMPLETED;
        }
        if (text.contains("expense") || text.contains("reimbursement")) {
            return ChatIntent.SUBMIT_EXPENSE;
        }
        return ChatIntent.UNKNOWN;
    }
}
