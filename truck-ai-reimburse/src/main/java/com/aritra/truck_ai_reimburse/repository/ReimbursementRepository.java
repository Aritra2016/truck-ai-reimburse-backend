package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReimbursementRepository extends JpaRepository<Reimbursement, Long> {

    Optional<Reimbursement> findByTripIdAndDestinationId(
            Long tripId,
            Long destinationId
    );
}
