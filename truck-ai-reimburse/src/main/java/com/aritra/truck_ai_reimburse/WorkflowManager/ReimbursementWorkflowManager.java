package com.aritra.truck_ai_reimburse.WorkflowManager;

import com.aritra.truck_ai_reimburse.Domain.ReimbursementSession;
import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.ExpenseBillRepository;
import com.aritra.truck_ai_reimburse.repository.PodRepository;
import com.aritra.truck_ai_reimburse.service.ReimbursementFinalizationService;
import com.aritra.truck_ai_reimburse.service.ReimbursementSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReimbursementWorkflowManager {

    private final ReimbursementSessionService sessionService;
    private final PodRepository podRepository;
    private final ExpenseBillRepository expenseBillRepository;
    private final ReimbursementFinalizationService finalizationService;

    /**
     * STEP 1: Called when driver says "Trip Completed"
     */
    public ReimbursementSession startOrResume(
            String sessionId,
            String tripId) {

        return sessionService.getOrCreateSession(sessionId, tripId);
    }

    /**
     * STEP 2: Gate check before allowing expense uploads
     */
    public void validatePodVerified(String sessionId) {

        boolean podVerified =
                podRepository.existsBySessionIdAndStatus(
                        sessionId,
                        com.aritra.truck_ai_reimburse.enums.PodStatus.VERIFIED
                );

        if (!podVerified) {
            throw new BusinessException(
                    "POD not verified. Reimbursement cannot proceed."
            );
        }
    }

    /**
     * STEP 3: Move session to IN_PROGRESS once first expense is uploaded
     */
    public void markExpensePhaseStarted(
            ReimbursementSession session) {

        if (session.getStatus() == ReimbursementStatus.POD_VERIFIED) {
            sessionService.updateStatus(
                    session,
                    ReimbursementStatus.IN_PROGRESS
            );
        }
    }

    /**
     * STEP 4: Final gate before payout
     */
    public ReimbursementSession finalizeIfEligible(
            ReimbursementSession session) {

        // POD must be verified
        validatePodVerified(session.getSessionId());

        // At least one expense must exist
        if (expenseBillRepository
                .findBySessionId(session.getSessionId())
                .isEmpty()) {
            throw new BusinessException(
                    "No expense bills uploaded"
            );
        }

        // No unconfirmed bills allowed
        boolean unconfirmedExists =
                expenseBillRepository
                        .existsBySessionIdAndConfirmedFalse(
                                session.getSessionId()
                        );

        if (unconfirmedExists) {
            throw new BusinessException(
                    "All expense bills must be confirmed"
            );
        }

        return finalizationService.finalizeReimbursement(session);
    }

}
