package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.service.SettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    /**
     * Initiate payment for a reimbursement
     */
    @PostMapping("/initiate/{reimbursementId}")
    public ResponseEntity<?> initiate(@PathVariable Long reimbursementId) {
        return ResponseEntity.ok(
                settlementService.initiateSettlement(reimbursementId)
        );
    }

    /**
     * Payment success callback (internal / scheduler)
     */
    @PostMapping("/{settlementId}/paid")
    public ResponseEntity<?> markPaid(@PathVariable Long settlementId) {
        settlementService.markPaid(settlementId);
        return ResponseEntity.ok("Payment marked as PAID");
    }

    /**
     * Payment failure callback
     */
    @PostMapping("/{settlementId}/failed")
    public ResponseEntity<?> markFailed(@PathVariable Long settlementId) {
        settlementService.markFailed(settlementId);
        return ResponseEntity.ok("Payment marked as FAILED");
    }
}
