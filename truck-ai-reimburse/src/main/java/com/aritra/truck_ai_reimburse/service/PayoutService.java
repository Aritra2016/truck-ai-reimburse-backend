package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Payout;
import com.aritra.truck_ai_reimburse.repository.PayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
//@RequiredArgsConstructor
public class PayoutService {

    private final PayoutRepository payoutRepository;


    public PayoutService(PayoutRepository payoutRepository) {
        this.payoutRepository = payoutRepository;
    }

    public Payout initiatePayOut(Payout payout){
        payout.setStatus("INITIATED");
        payout.setProcessedAt(LocalDateTime.now());
        return payoutRepository.save(payout);
    }


}
