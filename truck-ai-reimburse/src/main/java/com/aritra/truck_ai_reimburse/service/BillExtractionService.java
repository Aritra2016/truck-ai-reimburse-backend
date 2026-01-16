package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BillExtractionService {

    public Map<String, String> extractFields(String text) {

        Map<String, String> data = new HashMap<>();
        String lower = text.toLowerCase();

        // ✅ AMOUNT (₹, rs, total, amount)
        Pattern amountPattern = Pattern.compile(
                "(₹|rs\\.?|inr)?\\s*([0-9]{2,6}(\\.\\d{1,2})?)"
        );
        Matcher amountMatcher = amountPattern.matcher(lower);

        if (amountMatcher.find()) {
            data.put("amount", amountMatcher.group(2));
        }

        // ✅ CATEGORY
        if (lower.contains("fuel") || lower.contains("petrol") || lower.contains("diesel")) {
            data.put("category", "FUEL");
        } else if (lower.contains("toll")) {
            data.put("category", "TOLL");
        } else if (lower.contains("parking")) {
            data.put("category", "PARKING");
        }

        return data;
    }

    public BigDecimal extractFinalAmount(String text) {

        String normalized = text
                .replaceAll(",", "")
                .toLowerCase();

        Pattern pattern = Pattern.compile(
                "(total amount|grand total|amount paid|net amount|fuel total)\\s*[:\\-]?\\s*([$₹]?\\s*[0-9]+(\\.[0-9]{1,2})?)"
        );

        Matcher matcher = pattern.matcher(normalized);

        if (matcher.find()) {

            String amountStr = matcher.group(2)
                    .replaceAll("[^0-9.]", "");

            BigDecimal amount = new BigDecimal(amountStr);

            // ✅ SANITY CHECK — PLACE IT HERE
            if (amount.compareTo(new BigDecimal("10000")) > 0) {
                throw new BusinessException(
                        "Suspicious amount detected: " + amount
                );
            }

            return amount;
        }


        throw new BusinessException("Valid total amount not found in bill");
    }
}

