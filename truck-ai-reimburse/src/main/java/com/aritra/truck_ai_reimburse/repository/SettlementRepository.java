package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettlementRepository  extends JpaRepository<Settlement, Long> {
    Optional<Settlement> findByReimbursement_Id(Long reimbursementId);
}
