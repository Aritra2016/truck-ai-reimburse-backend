package com.aritra.truck_ai_reimburse.Domain;

import com.aritra.truck_ai_reimburse.enums.ExpenseCategory;

import java.math.BigDecimal;

public class InterpretedBill {

    private final ExpenseCategory category;
    private final BigDecimal amount;
    private final String currency;
    private final BigDecimal amountInINR;

    public InterpretedBill(
            ExpenseCategory category,
            BigDecimal amount,
            String currency,
            BigDecimal amountInINR) {

        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.amountInINR = amountInINR;
    }

    public ExpenseCategory getCategory() {
        return category;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmountInINR() {
        return amountInINR;
    }
}
