package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.PodDocument;
import com.aritra.truck_ai_reimburse.enums.PodStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PodRepository extends JpaRepository<PodDocument,Long> {

    Optional<PodDocument> findBySessionId(String sessionId);

    Optional<PodDocument> findByTripId(String tripId);

    boolean existsBySessionIdAndStatus(String sessionId, PodStatus status);
}
