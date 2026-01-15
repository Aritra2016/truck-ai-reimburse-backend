package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Expense;
import com.aritra.truck_ai_reimburse.Domain.Reimbursement;
import com.aritra.truck_ai_reimburse.Interpreter.ExpenseOcrInterpreter;
import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
//@RequiredArgsConstructor
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final OCRService ocrService;
    private final ExpenseOcrInterpreter expenseOcrInterpreter;
    private final ReimbursementService reimbursementService;
    public ExpenseService(ExpenseRepository expenseRepository, OCRService ocrService, ExpenseOcrInterpreter expenseOcrInterpreter, ReimbursementService reimbursementService) {
        this.expenseRepository = expenseRepository;
        this.ocrService = ocrService;
        this.expenseOcrInterpreter = expenseOcrInterpreter;
        this.reimbursementService = reimbursementService;
    }

    /**
     * Upload expense document and process OCR
     */
    public Expense uploadExpense(
            Long reimbursementId,
            String expenseType,
            MultipartFile file
    ) {

        Reimbursement reimbursement =
                reimbursementService.getById(reimbursementId);

        if (reimbursement.getStatus() != ReimbursementStatus.DRAFT) {
            throw new BusinessException(
                    "Expenses can be uploaded only in DRAFT reimbursement state"
            );
        }
        // 1️⃣ OCR raw text
        String rawText = ocrService.extractRawText(file);
        // 2️⃣ Interpret OCR text
        BigDecimal amount =
                expenseOcrInterpreter.extractAmount(rawText);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Unable to extract valid amount from expense");
        }
        // 3️⃣ Build Expense entity
        Expense expense = Expense.builder()
                .expenseType(expenseType)
                .amount(amount)
                .currency("INR")
                .reimbursement(reimbursement)
                .expenseDate(LocalDateTime.now())
                .approved(false)
                .ocrStatus(OcrStatus.VERIFIED)
                .build();
        Expense savedExpense = expenseRepository.save(expense);
        // 4️⃣ Recalculate reimbursement total
        reimbursementService.recalculateTotal(reimbursementId);

        return savedExpense;
    }

    /**
     * Get all expenses under a reimbursement
     */
    public List<Expense> getExpensesByReimbursement(Long reimbursementId) {
        return expenseRepository.findByReimbursement_Id(reimbursementId);
    }

    /**
     * Admin / system approval
     */
    public Expense approveExpense(Long expenseId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() ->
                        new BusinessException("Expense not found")
                );

        expense.setApproved(true);
        return expenseRepository.save(expense);
    }
}
