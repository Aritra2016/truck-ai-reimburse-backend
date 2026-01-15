package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.LedgerEvents;
import com.aritra.truck_ai_reimburse.Domain.PayStatement;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.enums.PayStatementStatus;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
import com.aritra.truck_ai_reimburse.repository.ExpenseRepository;
import com.aritra.truck_ai_reimburse.repository.PayRuleRepository;
import com.aritra.truck_ai_reimburse.repository.PayStatementRepository;
import com.aritra.truck_ai_reimburse.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
//@RequiredArgsConstructor
public class PayCalculationService {

    private static final Logger log = LoggerFactory.getLogger(PayCalculationService.class);

    private final PayStatementRepository payStatementRepository;
    private final TripRepository tripRepository;
   // private final ExpenseRepository expenseRepository;
   // private final PayRuleRepository payRuleRepository;
    private final LedgerService ledgerService;

    public PayCalculationService(PayStatementRepository payStatementRepository, TripRepository tripRepository, ExpenseRepository expenseRepository, PayRuleRepository payRuleRepository, LedgerService ledgerService) {
        this.payStatementRepository = payStatementRepository;
        this.tripRepository = tripRepository;
      //  this.expenseRepository = expenseRepository;
      //  this.payRuleRepository = payRuleRepository;
        this.ledgerService = ledgerService;
    }

    @Transactional
    public PayStatement calculatePay(Long id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.POD_VERIFIED) {
            throw new IllegalStateException("POD not verified");
        }

        // calculation logic (already discussed)
        BigDecimal totalPay = BigDecimal.valueOf(1000);

        PayStatement statement = PayStatement.builder()
                .trip(trip)
                .totalAmount(totalPay)
                .status(PayStatementStatus.CALCULATED)
                .build();

        payStatementRepository.save(statement);

        trip.setStatus(TripStatus.PAY_CALCULATED);
        tripRepository.save(trip);

        ledgerService.recordEvent(
                trip,
                LedgerEvents.PAY_CALCULATED,
                "Pay calculated",
                "TotalPay=" + totalPay
        );

        return statement;
    }
}
