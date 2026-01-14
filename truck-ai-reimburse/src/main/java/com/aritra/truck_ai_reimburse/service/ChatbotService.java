package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.ChatMessageDTO;
import com.aritra.truck_ai_reimburse.Domain.ChatRequest;
import com.aritra.truck_ai_reimburse.Domain.ChatResponse;
import com.aritra.truck_ai_reimburse.enums.ChatIntent;
import com.aritra.truck_ai_reimburse.exception.DocumentProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final OCRService ocrService;
    private final BillExtractionService billExtractionService;
    private final PdfTextService pdfTextService;

    public ChatResponse processMessage(ChatRequest request) {
        // 1. Generate sessionId if missing
        String sessionId = request.getSessionId();
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
            log.info("New session created: {}", sessionId);
        }
        //
        if (request == null || request.getMessage() == null) {
            return new ChatResponse(
                    request != null ? request.getSessionId() : null,
                    "Invalid request",
                    "NONE"
            );
        }
        log.info("Chatbot message received: {}", request.getMessage());
        String message = request.getMessage().toLowerCase();
        if (message.contains("pay")) {
            return new ChatResponse(
                    request.getSessionId(),
                    "Your last trip pay is being processed.",
                    "NONE"
            );
        }
        if (message.contains("trip is done") || message.contains("trip completed")) {
            return new ChatResponse(
                    request.getSessionId(),
                    "Please upload your expense receipts.",
                    "UPLOAD_BILL"
            );
        }

        return new ChatResponse(
                request.getSessionId(),
                "Sorry, I didn’t understand your request.",
                "NONE"
        );
    }
    public ChatResponse processBillUpload(MultipartFile file, String sessionId) throws DocumentProcessingException {
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new DocumentProcessingException("Invalid file");
        }
        filename = filename.toLowerCase();

        if (!filename.endsWith(".jpg") &&
                !filename.endsWith(".jpeg") &&
                !filename.endsWith(".png") &&
                !filename.endsWith(".pdf")) {
            throw new DocumentProcessingException("Unsupported file type");
        }

        String extractedText;
        if (filename.endsWith(".pdf")) {
            extractedText = pdfTextService.extractText(file);
        } else {
            extractedText = ocrService.extractText(file);
        }

        if (extractedText == null || extractedText.isBlank()) {
            throw new DocumentProcessingException("No readable text found in document");
        }

        log.info("===== OCR / PDF TEXT =====");
        log.info(extractedText);

        Map<String, String> billData =
                billExtractionService.extractFields(extractedText);

        if (billData.isEmpty()
                || billData.get("amount") == null
                || billData.get("category") == null) {

            return new ChatResponse(
                    sessionId,
                    "❌ This document does not look like a valid expense receipt. " +
                            "Please upload a fuel, toll, or parking bill with a visible amount.",
                    "UPLOAD_AGAIN"
            );
        }

        return new ChatResponse(
                sessionId,
                "✅ I found a " + billData.get("category") +
                        " expense of ₹" + billData.get("amount") +
                        ". Please confirm.",
                "CONFIRM_BILL"
        );

        }

    }
