package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.ChatMessageDTO;
import com.aritra.truck_ai_reimburse.Domain.ChatRequest;
import com.aritra.truck_ai_reimburse.Domain.ChatResponse;
import com.aritra.truck_ai_reimburse.enums.ChatIntent;
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
    public ChatResponse processBillUpload(MultipartFile file, String sessionId) {

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }

        String extractedText = ocrService.extractText(file);

        log.info("========== OCR TEXT START ==========");
        log.info(extractedText);
        log.info("========== OCR TEXT END ==========");

        Map<String, String> billData =
                billExtractionService.extractFields(extractedText);

        String category = billData.getOrDefault("category", "UNKNOWN");
        String amount = billData.getOrDefault("amount", "100");

        return new ChatResponse(
                sessionId,
                "I found a " + category +
                        " expense of ₹" + amount +
                        ". Please confirm.",
                "CONFIRM_BILL"
        );
    }
}
