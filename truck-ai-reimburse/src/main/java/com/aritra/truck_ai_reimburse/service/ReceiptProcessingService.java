package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.ReceiptUploadDTO;
import com.aritra.truck_ai_reimburse.Domain.Expense;
import com.aritra.truck_ai_reimburse.Domain.LedgerEvents;
import com.aritra.truck_ai_reimburse.Domain.Receipt;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
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
    private final PayCalculationService payCalculationService;

    //constructor
    public ReceiptProcessingService(ReceiptRepository receiptRepository, ExpenseRepository expenseRepository, LedgerService ledgerService, PayCalculationService payCalculationService) {
        this.receiptRepository = receiptRepository;
        this.expenseRepository = expenseRepository;
        this.ledgerService = ledgerService;
        this.payCalculationService = payCalculationService;
    }

    public Receipt processReceipt(Receipt receipt) {
        receipt.setConfidenceScore(Math.random()); // simulate OCR confidence
        return receiptRepository.save(receipt);
    }

    @Transactional
    public Receipt uploadReceipt(ReceiptUploadDTO dto) {

        Expense expense = expenseRepository.findById(dto.getExpenseId())
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        Trip trip = expense.getTrip();

        // Business rule: POD allowed only after trip completion
        if (!trip.getStatus().equals(TripStatus.COMPLETED)) {
            throw new IllegalStateException("Trip not completed yet");
        }

        Receipt receipt = Receipt.builder()
                .expense(expense)
                .trip(trip)
                .fileName(dto.getFileName())
                .fileUrl(dto.getFileUrl())
                .confidenceScore(dto.getConfidenceScore())
                .type("POD")
                .verified(dto.getConfidenceScore() != null
                        && dto.getConfidenceScore() >= 0.80)
                .uploadedAt(LocalDateTime.now())
                .build();

        receiptRepository.save(receipt);

        // Ledger entry
       ledgerService.recordEvent(trip, "POD_UPLOADED","Pool Car","nice");


        // Optional auto-pay trigger
        if (receipt.isVerified()) {
            payCalculationService.calculatePay(trip.getTrip_id());
        }

        return receipt;
    }

    @Transactional
    public Receipt uploadPOD(ReceiptUploadDTO dto) {

        Expense expense = expenseRepository.findById(dto.getExpenseId())
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        Trip trip = expense.getTrip();
        if (trip.getStatus() != (String.valueOf((TripStatus.COMPLETED))) ) {
            throw new IllegalStateException("Trip not completed");
        }

        Receipt receipt = Receipt.builder()
                .expense(expense)
                .trip(trip)
                .fileName(dto.getFileName())
                .fileUrl(dto.getFileUrl())
                .confidenceScore(dto.getConfidenceScore())
                .verified(dto.getConfidenceScore() != null && dto.getConfidenceScore() >= 0.8)
                .type("POD")
                .uploadedAt(LocalDateTime.now())
                .build();

        receiptRepository.save(receipt);

        trip.setStatus(String.valueOf(TripStatus.POD_UPLOADED));

        ledgerService.recordEvent(
                trip,
                LedgerEvents.POD_UPLOADED,
                "POD uploaded for expenseId=" + expense.getId(),
                "ConfidenceScore=" + dto.getConfidenceScore()
        );

        return receipt;
    }
}
