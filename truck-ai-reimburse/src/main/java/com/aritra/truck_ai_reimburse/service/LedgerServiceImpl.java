package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.Domain.TripLedger;
import com.aritra.truck_ai_reimburse.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerServiceImpl implements  LedgerService{

    private final LedgerRepository ledgerRepository;

        @Override
        public void recordEvent(Trip trip, String eventType, String eventDetails, String remarks) {

            TripLedger ledger = TripLedger.builder()
                    .trip(trip)
                    .eventType(eventType)
                    .eventDetails(eventDetails)
                    .eventTime(LocalDateTime.now())
                    .ledgerRef(UUID.randomUUID().toString())
                    .remarks(remarks)
                    .createdAt(LocalDateTime.now())
                    .build();

            ledgerRepository.save(ledger);
        }

    }

