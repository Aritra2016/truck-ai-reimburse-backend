package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.ReimbursementSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReimbursementSessionRepository extends JpaRepository<ReimbursementSession,Long> {

    Optional<ReimbursementSession> findBySessionId(String sessionId);

    Optional<ReimbursementSession> findByTripId(String tripId);

    boolean existsBySessionId(String sessionId);
}
