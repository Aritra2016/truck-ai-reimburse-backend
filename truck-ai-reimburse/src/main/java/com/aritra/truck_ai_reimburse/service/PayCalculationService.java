package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.PayStatement;
import com.aritra.truck_ai_reimburse.Domain.Trip;
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
    private final ExpenseRepository expenseRepository;
    private final PayRuleRepository payRuleRepository;
    private final LedgerService ledgerService;

    public PayCalculationService(PayStatementRepository payStatementRepository, TripRepository tripRepository, ExpenseRepository expenseRepository, PayRuleRepository payRuleRepository, LedgerService ledgerService) {
        this.payStatementRepository = payStatementRepository;
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.payRuleRepository = payRuleRepository;
        this.ledgerService = ledgerService;
    }

    @Transactional
    public PayStatement calculatePay(Trip trip) {

        log.info("Starting pay calculation for tripId={}", trip.getId());

        // Mock calculation logic (replace later with rule engine)
        BigDecimal grossPay = BigDecimal.valueOf(1500);
        BigDecimal deductions = BigDecimal.valueOf(200);
        BigDecimal netPay = grossPay.subtract(deductions);

        
        PayStatement statement = PayStatement.builder()
                .statementNumber("PS-" + System.currentTimeMillis())
                .driver(trip.getDriver())
                .trip(trip)
                .grossPay(grossPay)
                .deductions(deductions)
                .netPay(netPay)
                .generatedAt(LocalDateTime.now())
                .build();

        PayStatement savedStatement = payStatementRepository.save(statement);

        log.info("Pay calculation completed. StatementNumber={}, NetPay={}",
                savedStatement.getStatementNumber(),
                savedStatement.getNetPay());

        return savedStatement;
    }
}
