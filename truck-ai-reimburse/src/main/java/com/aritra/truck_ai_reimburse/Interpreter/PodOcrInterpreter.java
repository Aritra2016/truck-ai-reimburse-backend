package com.aritra.truck_ai_reimburse.Interpreter;

import com.aritra.truck_ai_reimburse.Domain.CurrencyDetector;
import com.aritra.truck_ai_reimburse.Domain.InterpretedBill;
import com.aritra.truck_ai_reimburse.service.BillExtractionService;
import com.aritra.truck_ai_reimburse.service.CurrencyConversionService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PodOcrInterpreter {

    private final BillExtractionService billExtractionService;
    private final CurrencyDetector currencyDetector;
    private final CurrencyConversionService conversionService;

    public PodOcrInterpreter(BillExtractionService billExtractionService, CurrencyDetector currencyDetector, CurrencyConversionService conversionService) {
        this.billExtractionService = billExtractionService;
        this.currencyDetector = currencyDetector;
        this.conversionService = conversionService;
    }

    public boolean isPodValid(String ocrText, String destinationName) {

        if (ocrText == null || ocrText.isBlank()) {
            return false;
        }

        // very basic rules (can be improved later)
        boolean hasSignature =
                ocrText.toLowerCase().contains("signature")
                        || ocrText.toLowerCase().contains("signed");

        boolean hasDestination =
                ocrText.toLowerCase().contains(destinationName.toLowerCase());

        return hasSignature && hasDestination;
    }

    public InterpretedBill interpret(String ocrText) {

        // 1️⃣ Detect currency from RAW OCR TEXT
        String currency =
                currencyDetector.detectCurrency(ocrText);

        // 2️⃣ Extract final payable amount
        BigDecimal amount =
                billExtractionService.extractFinalAmount(ocrText);

        // 3️⃣ Convert to INR
        BigDecimal amountInINR =
                conversionService.convertToINR(amount, currency);

        return new InterpretedBill(
                "FUEL",
                amount,
                currency,
                amountInINR
        );
    }

}
