package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.TripLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerRepository extends JpaRepository<TripLedger, Long> {

    List<TripLedger> findByTripId(Long tripId);

    List<TripLedger> findByEventType(String eventType);
}
