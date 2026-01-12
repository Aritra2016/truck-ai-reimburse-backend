package com.aritra.truck_ai_reimburse.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BillExtractionService {

    public Map<String, String> extractFields(String text) {

        Map<String, String> data = new HashMap<>();

        if (text == null || text.isBlank()) {
            return data;
        }

        String lowerText = text.toLowerCase();

        // ---------- CATEGORY ----------
        if (lowerText.contains("fuel")) {
            data.put("category", "FUEL");
        } else if (lowerText.contains("toll")) {
            data.put("category", "TOLL");
        } else if (lowerText.contains("tablet")
                || lowerText.contains("medicine")
                || lowerText.contains("gluco")) {
            data.put("category", "MEDICAL");
        } else {
            data.put("category", "OTHER");
        }

        // ---------- AMOUNT ----------
        Pattern amountPattern = Pattern.compile(
                "(rs\\.?|inr|₹)\\s*(\\d+(\\.\\d{1,2})?)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = amountPattern.matcher(text);

        if (matcher.find()) {
            data.put("amount", matcher.group(2));
        }

        return data;
    }

}
