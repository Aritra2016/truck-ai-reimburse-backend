package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.LedgerEvents;
import com.aritra.truck_ai_reimburse.Domain.Payout;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
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

    public PayoutService(PayoutRepository payoutRepository, LedgerService ledgerService, TripRepository tripRepository) {
        this.payoutRepository = payoutRepository;
        this.ledgerService = ledgerService;
        this.tripRepository = tripRepository;
    }

    public Payout initiatePayOut(Payout payout){
        payout.setStatus("INITIATED");
        payout.setProcessedAt(LocalDateTime.now());
        return payoutRepository.save(payout);
    }

    @Transactional
    public void completePayout(Payout payout, String txnRef) {

        payout.setStatus("PAID");
        payout.setTransactionRef(txnRef);
        payoutRepository.save(payout);

      Trip trip = payout.getPayStatement().getTrip();
    trip.setStatus(String.valueOf(TripStatus.PAID));
    tripRepository.save(trip);

//        Trip trip = tripRepository.findById(payout.getPayStatement())
//                .orElseThrow(() -> new IllegalStateException("Trip not found for id: " + payout.getPayStatement()));
//        trip.setStatus(TripStatus.PAID.name());

        ledgerService.recordEvent(
                trip,
                LedgerEvents.PAYOUT_DONE,
                "Amount credited to driver",
                "TxnRef=" + txnRef
        );
    }

}
