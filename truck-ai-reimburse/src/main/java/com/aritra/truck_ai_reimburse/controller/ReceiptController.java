package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.DTOs.ReceiptUploadDTO;
import com.aritra.truck_ai_reimburse.Domain.Receipt;
import com.aritra.truck_ai_reimburse.service.ReceiptProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptProcessingService receiptProcessingService;

    @PostMapping("/create")
    public Receipt uploadReceipt(@RequestBody Receipt receipt) {
        return receiptProcessingService.processReceipt(receipt);
    }

    @PostMapping("/receipts/upload")
    public ResponseEntity<Receipt> uploadReceipt(
            @Valid @RequestBody ReceiptUploadDTO dto) {

        Receipt receipt = receiptProcessingService.uploadReceipt(dto);
        return ResponseEntity.ok(receipt);
    }
}
