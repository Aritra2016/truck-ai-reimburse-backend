package com.aritra.truck_ai_reimburse.Domain;


import com.aritra.truck_ai_reimburse.enums.ExpenseCategory;
import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_bill")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;
    private String tripId;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    private BigDecimal amount;
    private String currency;
    private BigDecimal amountInINR;

    private boolean confirmed;

    @Enumerated(EnumType.STRING)
    private OcrStatus ocrStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmountInINR() {
        return amountInINR;
    }

    public void setAmountInINR(BigDecimal amountInINR) {
        this.amountInINR = amountInINR;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public OcrStatus getOcrStatus() {
        return ocrStatus;
    }

    public void setOcrStatus(OcrStatus ocrStatus) {
        this.ocrStatus = ocrStatus;
    }
}
