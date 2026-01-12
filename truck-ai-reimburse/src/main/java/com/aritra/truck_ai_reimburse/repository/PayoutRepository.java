package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.Payout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface PayoutRepository extends JpaRepository<Payout, Long> {

    Optional<Payout> findByPayoutRef(String payoutRef);

    List<Payout> findByDriverId(Long driverId);

    List<Payout> findByStatus(String status);
}
