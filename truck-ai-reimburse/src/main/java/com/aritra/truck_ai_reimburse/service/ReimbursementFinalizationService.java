package com.aritra.truck_ai_reimburse.service;


import com.aritra.truck_ai_reimburse.Domain.AuditLog;
import com.aritra.truck_ai_reimburse.Domain.ExpenseBill;
import com.aritra.truck_ai_reimburse.Domain.ReimbursementSession;
import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import com.aritra.truck_ai_reimburse.repository.AuditRepository;
import com.aritra.truck_ai_reimburse.repository.ExpenseBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReimbursementFinalizationService {

    private final ExpenseBillRepository expenseBillRepository;
    private final ReimbursementSessionService sessionService;
    private final AuditRepository auditRepository;

    public ReimbursementSession finalizeReimbursement(
            ReimbursementSession session) {

        List<ExpenseBill> bills =
                expenseBillRepository.findBySessionId(
                        session.getSessionId()
                );

        BigDecimal total = bills.stream()
                .filter(ExpenseBill::isConfirmed)
                .map(ExpenseBill::getAmountInINR)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        session.setTotalAmountInINR(total);
        sessionService.updateStatus(
                session,
                ReimbursementStatus.READY_FOR_PAYOUT
        );

        auditRepository.save(
                AuditLog.builder()
                        .entityName("REIMBURSEMENT")
                        .entityId(session.getId().toString())
                        .action("READY_FOR_PAYOUT")
                        .performedAt(LocalDateTime.now())
                        .build()
        );

        return session;
    }


}
