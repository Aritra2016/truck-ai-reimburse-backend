package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Reimbursement;
import com.aritra.truck_ai_reimburse.Domain.Settlement;
import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import com.aritra.truck_ai_reimburse.enums.SettlementStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.SettlementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final ReimbursementService reimbursementService;

    public SettlementService(
            SettlementRepository settlementRepository,
            ReimbursementService reimbursementService
    ) {
        this.settlementRepository = settlementRepository;
        this.reimbursementService = reimbursementService;
    }

    /**
     * Initiate settlement after reimbursement confirmation
     */
    @Transactional
    public Settlement initiateSettlement(Long reimbursementId) {

        Reimbursement reimbursement =
                reimbursementService.getById(reimbursementId);

        // 1️⃣ Reimbursement must be READY
        if (reimbursement.getStatus() != ReimbursementStatus.READY_FOR_SETTLEMENT) {
            throw new BusinessException(
                    "Settlement allowed only after reimbursement confirmation"
            );
        }

        // 2️⃣ Prevent duplicate settlement
        settlementRepository.findByReimbursement_Id(reimbursementId)
                .ifPresent(s -> {
                    throw new BusinessException("Settlement already exists");
                });

        // 3️⃣ Create settlement
        Settlement settlement = new Settlement();
        settlement.setReimbursement(reimbursement);
        settlement.setAmount(reimbursement.getTotalAmount());
        settlement.setStatus(SettlementStatus.INITIATED);
        settlement.setInitiatedAt(LocalDateTime.now());

        Settlement saved = settlementRepository.save(settlement);

        // 4️⃣ Trigger payment (async / external system)
        // paymentGateway.pay(saved);

        return saved;
    }

    /**
     * Mark payment success (callback / scheduler)
     */
    @Transactional
    public void markPaid(Long settlementId) {

        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() ->
                        new BusinessException("Settlement not found")
                );

        settlement.setStatus(SettlementStatus.PAID);
        settlement.setCompletedAt(LocalDateTime.now());

        settlementRepository.save(settlement);
    }

    /**
     * Mark payment failure
     */
    @Transactional
    public void markFailed(Long settlementId) {

        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() ->
                        new BusinessException("Settlement not found")
                );

        settlement.setStatus(SettlementStatus.FAILED);
        settlementRepository.save(settlement);
    }
}
