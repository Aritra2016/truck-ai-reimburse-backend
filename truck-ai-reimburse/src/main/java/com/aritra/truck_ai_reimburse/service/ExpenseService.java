package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.ExpenseUploadDTO;
import com.aritra.truck_ai_reimburse.Domain.Destination;
import com.aritra.truck_ai_reimburse.Domain.Expense;
import com.aritra.truck_ai_reimburse.Domain.Reimbursement;
import com.aritra.truck_ai_reimburse.Interpreter.ExpenseOcrInterpreter;
import com.aritra.truck_ai_reimburse.enums.DestinationStatus;
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

    private final DestinationService destinationService;

    public ExpenseService(ExpenseRepository expenseRepository, OCRService ocrService, ExpenseOcrInterpreter expenseOcrInterpreter, ReimbursementService reimbursementService, DestinationService destinationService) {
        this.expenseRepository = expenseRepository;
        this.destinationService = destinationService;
    }
//    Upload expense document and process OCR
//    public Expense uploadExpense(
//            Long reimbursementId,
//            String expenseType,
//            MultipartFile file
//    ) {
//        Reimbursement reimbursement =
//                reimbursementService.getById(reimbursementId);
//        if (reimbursement.getStatus() != ReimbursementStatus.DRAFT) {
//            throw new BusinessException(
//                    "Expenses can be uploaded only in DRAFT reimbursement state"
//            );
//        }
//        //  OCR raw text
//        String rawText = ocrService.extractRawText(file);
//        // Interpret OCR text
//        BigDecimal amount =
//                expenseOcrInterpreter.extractAmount(rawText);
//        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new BusinessException("Unable to extract valid amount from expense");
//        }
//        Expense expense = Expense.builder()
//                .type(expenseType)
//                .amount(amount)
//                .currency("INR")
//                .reimbursement(reimbursement)
//                .expenseDate(LocalDateTime.now())
//                .approved(false)
//                .ocrStatus(OcrStatus.VERIFIED)
//                .build();
//        Expense savedExpense = expenseRepository.save(expense);
//        reimbursementService.recalculateTotal(reimbursementId);
//        return savedExpense;
//    }

    public Expense uploadExpense(ExpenseUploadDTO dto) {
        Destination current =
                destinationService.getCurrentDestination(dto.getTripId());
        if (current == null ||
                current.getStatus() != DestinationStatus.VERIFIED) {
            throw new BusinessException(
                    "Upload Proof of Arrival before expenses"
            );
        }
        Expense expense = Expense.builder()
                .type(dto.getExpenseType())
                .amount(dto.getAmount())
                .currency("INR")
                .reimbursement(
                        reimbursementService.getById(dto.getReimbursementId())
                )
                .expenseDate(LocalDateTime.now())
                .approved(false)
                .ocrStatus(OcrStatus.VERIFIED)
                .build();

        Expense saved = expenseRepository.save(expense);
        reimbursementService.recalculateTotal(dto.getReimbursementId());

        return saved;
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
