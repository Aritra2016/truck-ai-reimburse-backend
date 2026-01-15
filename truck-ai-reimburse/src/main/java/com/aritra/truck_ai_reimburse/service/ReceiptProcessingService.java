package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.ReceiptUploadDTO;
import com.aritra.truck_ai_reimburse.Domain.*;
import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.ExpenseRepository;
import com.aritra.truck_ai_reimburse.repository.ReceiptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//@RequiredArgsConstructor
@Service
public class ReceiptProcessingService {

    private final ReceiptRepository receiptRepository;
   private final ExpenseRepository expenseRepository;
   private final LedgerService ledgerService;
  //  private final PayCalculationService payCalculationService;

    //constructor
    public ReceiptProcessingService(ReceiptRepository receiptRepository, ExpenseRepository expenseRepository, LedgerService ledgerService, PayCalculationService payCalculationService, LedgerService ledgerService1) {
        this.receiptRepository = receiptRepository;
        this.expenseRepository = expenseRepository;
     //   this.ledgerService = ledgerService;
    //    this.payCalculationService = payCalculationService;
        this.ledgerService = ledgerService1;
    }
    public Receipt processReceipt(Receipt receipt) {
        receipt.setConfidenceScore(Math.random()); // simulate OCR confidence
        return receiptRepository.save(receipt);
    }
    @Transactional
    public Receipt uploadReceipt(ReceiptUploadDTO dto) {

        // 1️⃣ Fetch Expense
        Expense expense = expenseRepository.findById(dto.getExpenseId())
                .orElseThrow(() ->
                        new BusinessException("Expense not found")
                );

        // 2️⃣ Validate reimbursement state
        Reimbursement reimbursement = expense.getReimbursement();

        if (reimbursement.getStatus() != ReimbursementStatus.DRAFT) {
            throw new BusinessException(
                    "Receipt upload allowed only in DRAFT reimbursement"
            );
        }

        // 3️⃣ Decide verification based on OCR confidence
        boolean verified =
                dto.getConfidenceScore() != null
                        && dto.getConfidenceScore() >= 0.80;

        // 4️⃣ Build Receipt entity
        Receipt receipt = Receipt.builder()
                .expense(expense)
                .fileName(dto.getFileName())
                .fileUrl(dto.getFileUrl())
                .confidenceScore(dto.getConfidenceScore())
                .verified(verified)
                .uploadedAt(LocalDateTime.now())
                .build();

        return receiptRepository.save(receipt);
    }

//    @Transactional
//    public Receipt uploadPOD(ReceiptUploadDTO dto) {
//        Expense expense = expenseRepository.findById(dto.getExpenseId())
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//        Trip trip = expense.getTrip();
//        if (trip.getStatus() != TripStatus.COMPLETED ) {
//            throw new IllegalStateException("Trip not completed");
//        }
//
//        Receipt receipt = Receipt.builder()
//                .expense(expense)
//                .trip(trip)
//                .fileName(dto.getFileName())
//                .fileUrl(dto.getFileUrl())
//                .confidenceScore(dto.getConfidenceScore())
//                .verified(dto.getConfidenceScore() != null && dto.getConfidenceScore() >= 0.8)
//                .type("POD")
//                .uploadedAt(LocalDateTime.now())
//                .build();
//
//        receiptRepository.save(receipt);
//
//        trip.setStatus(TripStatus.POD_UPLOADED);
//
//        ledgerService.recordEvent(
//                trip,
//                LedgerEvents.POD_UPLOADED,
//                "POD uploaded for expenseId=" + expense.getId(),
//                "ConfidenceScore=" + dto.getConfidenceScore()
//        );
//
//        return receipt;
//    }



}
