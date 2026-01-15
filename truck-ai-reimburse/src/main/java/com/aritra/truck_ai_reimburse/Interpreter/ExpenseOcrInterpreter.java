package com.aritra.truck_ai_reimburse.Interpreter;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExpenseOcrInterpreter {

    /**
     * Extracts the most probable expense amount from OCR text
     */
    public BigDecimal extractAmount(String ocrText) {

        if (ocrText == null || ocrText.isBlank()) {
            return null;
        }

        // Normalize text
        String text = ocrText
                .replace(",", "")
                .replace("\n", " ")
                .toLowerCase();

        // Regex to match amounts like:
        // ₹123.45 | rs 123 | inr 123.00 | total 123
        Pattern pattern = Pattern.compile(
                "(₹|rs\\.?|inr)?\\s*(\\d+(?:\\.\\d{1,2})?)"
        );

        Matcher matcher = pattern.matcher(text);

        List<BigDecimal> amounts = new ArrayList<>();

        while (matcher.find()) {
            try {
                String value = matcher.group(2);
                BigDecimal amount = new BigDecimal(value);
                amounts.add(amount);
            } catch (Exception ignored) {
            }
        }

        if (amounts.isEmpty()) {
            return null;
        }

        // Strategy: return the highest amount
        // (usually TOTAL amount on bill)
        return amounts.stream()
                .max(BigDecimal::compareTo)
                .orElse(null);
    }


}
