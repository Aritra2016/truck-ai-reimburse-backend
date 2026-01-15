package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.LedgerEvents;
import com.aritra.truck_ai_reimburse.Domain.PayStatement;
import com.aritra.truck_ai_reimburse.Domain.Payout;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.enums.PayoutStatus;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
import com.aritra.truck_ai_reimburse.repository.PayStatementRepository;
import com.aritra.truck_ai_reimburse.repository.PayoutRepository;
import com.aritra.truck_ai_reimburse.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
//@RequiredArgsConstructor
public class PayoutService {
    private final PayoutRepository payoutRepository;
    private final LedgerService ledgerService;
    private final TripRepository tripRepository;
    private final PayStatementRepository payStatementRepository;

    public PayoutService(PayoutRepository payoutRepository, LedgerService ledgerService, TripRepository tripRepository, PayStatementRepository payStatementRepository) {
        this.payoutRepository = payoutRepository;
        this.ledgerService = ledgerService;
        this.tripRepository = tripRepository;
        this.payStatementRepository = payStatementRepository;
    }
    public Payout initiatePayOut(Payout payout){

        if (payout.getPayStatement() == null || payout.getPayStatement().getId() == null) {
            throw new IllegalArgumentException("PayStatement id is required");
        }

        // OAD FULL PAY STATEMENT FROM DB
        PayStatement ps = payStatementRepository.findById(
                payout.getPayStatement().getId()
        ).orElseThrow(() ->
                new IllegalStateException("PayStatement not found")
        );

        // SET MANDATORY FKs
        payout.setPayStatement(ps);
        payout.setDriver(ps.getDriver());
        payout.setTrip(ps.getTrip());

        payout.setStatus(PayoutStatus.INITIATED);
        payout.setProcessedAt(LocalDateTime.now());

        return payoutRepository.save(payout);


    }

    @Transactional
    public void completePayout(Payout payout, String txnRef) {

        payout.setStatus(PayoutStatus.SUCCESS);
        payout.setTransactionRef(txnRef);
        payoutRepository.save(payout);

        Trip trip = payout.getPayStatement().getTrip();

        if (trip == null) {
            throw new IllegalStateException("Trip not found for payout " + payout.getId());
        }

        trip.setStatus(TripStatus.PAID);
        tripRepository.save(trip);

        ledgerService.recordEvent(
                trip,
                LedgerEvents.PAYOUT_DONE,
                "Amount credited to driver",
                "TxnRef=" + txnRef
        );
    }

}
