package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.DTOs.ExpenseBillUploadResponseDTO;
import com.aritra.truck_ai_reimburse.DTOs.ExpenseConfirmationDTO;
import com.aritra.truck_ai_reimburse.Domain.ExpenseBill;
import com.aritra.truck_ai_reimburse.Domain.InterpretedBill;
import com.aritra.truck_ai_reimburse.Interpreter.PodOcrInterpreter;
import com.aritra.truck_ai_reimburse.WorkflowManager.ReimbursementWorkflowManager;
import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import com.aritra.truck_ai_reimburse.service.BillExtractionService;
import com.aritra.truck_ai_reimburse.service.ExpenseBillService;
import com.aritra.truck_ai_reimburse.service.OCRService;
import com.aritra.truck_ai_reimburse.service.PdfTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseBillService expenseBillService;
    private final BillExtractionService billExtractionService;
    private final OCRService ocrService;
    private final PdfTextService pdfTextService;
    private final ReimbursementWorkflowManager workflowManager;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ExpenseBillUploadResponseDTO uploadExpense(
            @RequestParam("file") MultipartFile file,
            @RequestParam String sessionId,
            @RequestParam String tripId
    ) {

        String extractedText =
                file.getOriginalFilename() != null
                        && file.getOriginalFilename().endsWith(".pdf")
                        ? pdfTextService.extractText(file)
                        : ocrService.extractText(file);

        // ✅ CORRECT SERVICE FOR EXPENSE OCR
        InterpretedBill interpreted = billExtractionService.interpret(extractedText);

        ExpenseBill bill = ExpenseBill.builder()
                .sessionId(sessionId)
                .tripId(tripId)
                .category(interpreted.getCategory())
                .amount(interpreted.getAmount())
                .currency(interpreted.getCurrency())
                .amountInINR(interpreted.getAmountInINR())
                .confirmed(false)
                .ocrStatus(OcrStatus.VERIFIED)
                .build();

        ExpenseBill saved = expenseBillService.saveExtractedBill(bill);

        return ExpenseBillUploadResponseDTO.builder()
                .expenseBillId(saved.getId())
                .category(saved.getCategory())
                .amount(saved.getAmount())
                .currency(saved.getCurrency())
                .amountInINR(saved.getAmountInINR())
                .message("Please confirm this expense")
                .build();
    }

    @PostMapping("/confirm")
    public void confirmExpense(
            @RequestBody ExpenseConfirmationDTO dto
    ) {
        expenseBillService.confirmBill(
                dto.getExpenseBillId(),
                dto.isConfirmed()
        );
    }
}
