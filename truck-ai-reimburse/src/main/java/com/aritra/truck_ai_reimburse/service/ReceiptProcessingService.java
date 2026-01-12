package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Receipt;
import com.aritra.truck_ai_reimburse.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@RequiredArgsConstructor
@Service
public class ReceiptProcessingService {

    private final ReceiptRepository receiptRepository;

    public ReceiptProcessingService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt processReceipt(Receipt receipt) {
        receipt.setConfidenceScore(Math.random()); // simulate OCR confidence
        return receiptRepository.save(receipt);
    }
}
