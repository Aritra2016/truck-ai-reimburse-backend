package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {

    List<Dispute> findByStatus(String status);

    List<Dispute> findByDriverId(Long driverId);
}
