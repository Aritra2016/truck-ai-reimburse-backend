package com.aritra.truck_ai_reimburse.controller;


import com.aritra.truck_ai_reimburse.DTOs.AllDocumentsUploadedDTO;
import com.aritra.truck_ai_reimburse.DTOs.ReimbursementSummaryDTO;
import com.aritra.truck_ai_reimburse.Domain.ReimbursementSession;
import com.aritra.truck_ai_reimburse.WorkflowManager.ReimbursementWorkflowManager;
import com.aritra.truck_ai_reimburse.service.ReimbursementSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reimbursement")
@RequiredArgsConstructor
public class ReimbursementController {

    private final ReimbursementWorkflowManager workflowManager;
    private final ReimbursementSessionService sessionService;

    @PostMapping("/finalize")
    public ReimbursementSummaryDTO finalizeReimbursement(
            @RequestBody AllDocumentsUploadedDTO dto) {

        ReimbursementSession session =
                sessionService.getOrCreateSession(
                        dto.getSessionId(),
                        dto.getTripId()
                );

        ReimbursementSession finalized =
                workflowManager.finalizeIfEligible(session);

        return ReimbursementSummaryDTO.builder()
                .sessionId(finalized.getSessionId())
                .tripId(finalized.getTripId())
                .totalAmountInINR(finalized.getTotalAmountInINR())
                .status(finalized.getStatus())
                .message("Reimbursement ready for payout")
                .build();
    }


}
