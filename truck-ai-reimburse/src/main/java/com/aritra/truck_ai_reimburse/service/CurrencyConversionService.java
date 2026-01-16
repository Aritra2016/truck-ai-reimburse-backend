package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private static final Map<String, BigDecimal> RATE_TO_INR = Map.of(
            "USD", new BigDecimal("83.0"),
            "CAD", new BigDecimal("61.5"),
            "EUR", new BigDecimal("90.0"),
            "INR", BigDecimal.ONE
    );

    public BigDecimal convertToINR(BigDecimal amount, String currency) {

        BigDecimal rate = RATE_TO_INR.get(currency);

        if (rate == null) {
            throw new BusinessException("Unsupported currency: " + currency);
        }

        return amount.multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);
    }


}
