package com.aritra.truck_ai_reimburse.controller;


import com.aritra.truck_ai_reimburse.Domain.Payout;
import com.aritra.truck_ai_reimburse.service.PayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payouts")
@RequiredArgsConstructor
public class PayoutController {

    private final PayoutService payoutService;

    @PostMapping("/create")
    public Payout initiatePayout(@RequestBody Payout payout) {
        return payoutService.initiatePayOut(payout);
    }
}
