package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.ExpenseBill;
import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseBillRepository extends JpaRepository<ExpenseBill, Long> {

    List<ExpenseBill> findBySessionId(String sessionId);

    List<ExpenseBill> findByTripId(String tripId);

    List<ExpenseBill> findBySessionIdAndOcrStatus(String sessionId, OcrStatus status);

    boolean existsBySessionIdAndConfirmedFalse(String sessionId);

    Optional<ExpenseBill> findTopBySessionIdAndConfirmedFalseOrderByIdDesc(
            String sessionId
    );
}
