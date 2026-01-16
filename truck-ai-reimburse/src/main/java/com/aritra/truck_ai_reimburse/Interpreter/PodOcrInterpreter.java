package com.aritra.truck_ai_reimburse.Interpreter;

import com.aritra.truck_ai_reimburse.Domain.CurrencyDetector;
import com.aritra.truck_ai_reimburse.Domain.InterpretedBill;
import com.aritra.truck_ai_reimburse.enums.ExpenseCategory;
import com.aritra.truck_ai_reimburse.service.BillExtractionService;
import com.aritra.truck_ai_reimburse.service.CurrencyConversionService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Component
public class PodOcrInterpreter {

    private final BillExtractionService billExtractionService;
    private final CurrencyDetector currencyDetector;
    private final CurrencyConversionService conversionService;

    // simple, safe regex (no extra dependency)
    private static final Pattern DELIVERY_DATE_PATTERN =
            Pattern.compile("(delivery date|delivered on)", Pattern.CASE_INSENSITIVE);

    private static final Pattern DELIVERY_LOCATION_PATTERN =
            Pattern.compile("(delivery location|delivered at)", Pattern.CASE_INSENSITIVE);

    public PodOcrInterpreter(
            BillExtractionService billExtractionService,
            CurrencyDetector currencyDetector,
            CurrencyConversionService conversionService) {

        this.billExtractionService = billExtractionService;
        this.currencyDetector = currencyDetector;
        this.conversionService = conversionService;
    }

    /**
     * POD is valid if OCR text proves delivery happened.
     */
    public boolean isPodValid(String ocrText) {

        if (ocrText == null || ocrText.isBlank()) {
            return false;
        }

        String text = ocrText.toLowerCase();

        boolean hasSignature =
                text.contains("signature")
                        || text.contains("signed");

        boolean hasDeliveryDate =
                DELIVERY_DATE_PATTERN.matcher(text).find();

        boolean hasDeliveryLocation =
                DELIVERY_LOCATION_PATTERN.matcher(text).find();

        return hasSignature && hasDeliveryDate && hasDeliveryLocation;
    }

    // unchanged (used for expense OCR, kept as-is)
    public InterpretedBill interpret(String ocrText) {

        String currency = currencyDetector.detectCurrency(ocrText);
        BigDecimal amount = billExtractionService.extractFinalAmount(ocrText);
        BigDecimal amountInINR = conversionService.convertToINR(amount, currency);

        ExpenseCategory category = ExpenseCategory.FUEL;

        return new InterpretedBill(
                category,
                amount,
                currency,
                amountInINR
        );
    }
}
