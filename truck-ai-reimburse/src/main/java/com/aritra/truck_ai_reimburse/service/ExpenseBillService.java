package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.AuditLog;
import com.aritra.truck_ai_reimburse.Domain.ExpenseBill;
import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import com.aritra.truck_ai_reimburse.repository.AuditRepository;
import com.aritra.truck_ai_reimburse.repository.ExpenseBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpenseBillService {

    private final ExpenseBillRepository expenseBillRepository;
    private final AuditRepository auditRepository;

    public ExpenseBill saveExtractedBill(ExpenseBill bill) {
        bill.setOcrStatus(OcrStatus.VERIFIED);
        ExpenseBill saved = expenseBillRepository.save(bill);

        auditRepository.save(
                AuditLog.builder()
                        .entityName("EXPENSE_BILL")
                        .entityId(saved.getId().toString())
                        .action("BILL_EXTRACTED")
                        .performedAt(LocalDateTime.now())
                        .build()
        );

        return saved;
    }

    public void confirmBill(Long billId, boolean confirmed) {
        ExpenseBill bill = expenseBillRepository.findById(billId)
                .orElseThrow();

        bill.setConfirmed(confirmed);
        expenseBillRepository.save(bill);

        auditRepository.save(
                AuditLog.builder()
                        .entityName("EXPENSE_BILL")
                        .entityId(billId.toString())
                        .action("BILL_CONFIRMED")
                        .performedAt(LocalDateTime.now())
                        .build()
        );
    }


}
