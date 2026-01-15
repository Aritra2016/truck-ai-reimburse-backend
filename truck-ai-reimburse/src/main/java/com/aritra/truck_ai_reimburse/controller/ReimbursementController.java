package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.service.ReimbursementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reimbursements")
public class ReimbursementController {

    private final ReimbursementService service;

    public ReimbursementController(ReimbursementService service) {
        this.service = service;
    }

    /**
     * Called internally after POD verification (or by chatbot/UI)
     */
    @PostMapping("/open")
    public ResponseEntity<?> open(
            @RequestParam Long tripId,
            @RequestParam Long destinationId
    ) {
        return ResponseEntity.ok(
                service.openReimbursement(tripId, destinationId)
        );
    }

    /**
     * Driver confirms reimbursement
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.confirm(id)
        );
    }

}
