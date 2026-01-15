package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.Domain.Expense;
import com.aritra.truck_ai_reimburse.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Upload expense document (Fuel / Food / Toll etc.)
     * OCR will extract amount and update reimbursement total
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadExpense(
            @RequestParam Long reimbursementId,
            @RequestParam String expenseType,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                expenseService.uploadExpense(
                        reimbursementId,
                        expenseType,
                        file
                )
        );
    }

    /**
     * Get all expenses for a reimbursement
     */
    @GetMapping("/reimbursement/{reimbursementId}")
    public ResponseEntity<?> getExpensesByReimbursement(
            @PathVariable Long reimbursementId
    ) {
        return ResponseEntity.ok(
                expenseService.getExpensesByReimbursement(reimbursementId)
        );
    }

    /**
     * Approve an expense (Admin / System use)
     */
    @PostMapping("/{expenseId}/approve")
    public ResponseEntity<?> approveExpense(
            @PathVariable Long expenseId
    ) {
        return ResponseEntity.ok(
                expenseService.approveExpense(expenseId)
        );
    }
}
