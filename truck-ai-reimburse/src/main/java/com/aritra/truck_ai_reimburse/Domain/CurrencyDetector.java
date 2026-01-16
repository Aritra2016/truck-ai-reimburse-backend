package com.aritra.truck_ai_reimburse.Domain;


import com.aritra.truck_ai_reimburse.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class CurrencyDetector {

    public String detectCurrency(String text) {
        String lower = text.toLowerCase();

        // Canada-specific detection
        if (lower.contains("canada") || lower.contains("gst") || lower.contains("pst")) {
            return "CAD";
        }

        // Dollar-based fallback
        if (lower.contains("$")) {
            return "USD"; // fallback ONLY
        }

        if (lower.contains("₹") || lower.contains("rs")) {
            return "INR";
        }

        if (lower.contains("€")) {
            return "EUR";
        }

        throw new BusinessException("Unable to detect currency from bill");
    }

}
