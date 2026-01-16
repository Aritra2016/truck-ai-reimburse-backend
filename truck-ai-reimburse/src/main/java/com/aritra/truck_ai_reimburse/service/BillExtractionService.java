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

        // 1️⃣ STRONG PRIORITY: TOTAL AMOUNT
        Pattern totalAmountPattern = Pattern.compile(
                "(total amount)\\s*[:\\-]?\\s*([$₹]?\\s*[0-9]+(\\.[0-9]{1,2})?)"
        );

        Matcher totalMatcher = totalAmountPattern.matcher(normalized);
        if (totalMatcher.find()) {
            return parseAndValidateAmount(totalMatcher.group(2));
        }

        // 2️⃣ FALLBACKS (only if TOTAL AMOUNT not present)
        Pattern fallbackPattern = Pattern.compile(
                "(grand total|amount paid|net amount)\\s*[:\\-]?\\s*([$₹]?\\s*[0-9]+(\\.[0-9]{1,2})?)"
        );

        Matcher fallbackMatcher = fallbackPattern.matcher(normalized);
        if (fallbackMatcher.find()) {
            return parseAndValidateAmount(fallbackMatcher.group(2));
        }

        // 3️⃣ LAST RESORT (fuel total ONLY if nothing else exists)
        Pattern fuelTotalPattern = Pattern.compile(
                "(fuel total)\\s*[:\\-]?\\s*([$₹]?\\s*[0-9]+(\\.[0-9]{1,2})?)"
        );

        Matcher fuelMatcher = fuelTotalPattern.matcher(normalized);
        if (fuelMatcher.find()) {
            return parseAndValidateAmount(fuelMatcher.group(2));
        }

        throw new BusinessException("Final payable amount not found in bill");

    }

    // ✅ THIS IS THE MISSING METHOD (ADD THIS)
    private BigDecimal parseAndValidateAmount(String rawAmount) {

        String amountStr = rawAmount.replaceAll("[^0-9.]", "");
        BigDecimal amount = new BigDecimal(amountStr);

        // 🚨 Sanity check
        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            throw new BusinessException(
                    "Suspicious amount detected: " + amount
            );
        }

        return amount;

    }
}

