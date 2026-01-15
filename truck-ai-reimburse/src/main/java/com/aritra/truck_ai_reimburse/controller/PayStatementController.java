package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.Domain.PayStatement;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.service.PayCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay-statements")
@RequiredArgsConstructor
public class PayStatementController {

    private final PayCalculationService payCalculationService;

    @PostMapping("/calculate")
    public PayStatement calculatePay(@RequestBody Trip trip) {
        return payCalculationService.calculatePay(trip.getTrip_id());
    }
}
