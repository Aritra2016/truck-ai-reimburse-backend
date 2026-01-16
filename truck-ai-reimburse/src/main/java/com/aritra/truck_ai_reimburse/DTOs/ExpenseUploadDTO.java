package com.aritra.truck_ai_reimburse.DTOs;

import java.math.BigDecimal;

public class ExpenseUploadDTO {

    // primary key
    private Long tripId;

    // Expense details
    private String expenseType;   // FUEL, FOOD, TOLL, etc.
    private BigDecimal amount;

    // OCR / document info
    private String documentUrl;   // S3 / local path
    private String rawOcrText;    // optional (debug / audit)

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getRawOcrText() {
        return rawOcrText;
    }

    public void setRawOcrText(String rawOcrText) {
        this.rawOcrText = rawOcrText;
    }
}
