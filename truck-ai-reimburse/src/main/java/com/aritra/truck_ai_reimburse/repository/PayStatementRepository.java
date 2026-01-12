package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.PayStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayStatementRepository extends JpaRepository<PayStatement, Long> {

    Optional<PayStatement> findByStatementNumber(String statementNumber);

    List<PayStatement> findByDriverId(Long driverId);
}