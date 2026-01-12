package com.aritra.truck_ai_reimburse.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FXService {
    public BigDecimal convert(BigDecimal amount, String from, String to) {
        if (from.equals(to)) return amount;
        return amount.multiply(BigDecimal.valueOf(1.25)); // mock USD→CAD
    }

}
