package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.ChatMessageDTO;
import com.aritra.truck_ai_reimburse.Domain.ChatRequest;
import com.aritra.truck_ai_reimburse.Domain.ChatResponse;
import com.aritra.truck_ai_reimburse.Domain.InterpretedBill;
import com.aritra.truck_ai_reimburse.Interpreter.PodOcrInterpreter;
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
    private final PdfTextService pdfTextService;
    private final PodOcrInterpreter podOcrInterpreter; // ⭐ KEY DEPENDENCY

    /* -------------------------------
       TEXT MESSAGE HANDLING
    -------------------------------- */
    public ChatResponse processMessage(ChatRequest request) {

        if (request == null || request.getMessage() == null) {
            return new ChatResponse(
                    null,
                    "Invalid request",
                    ChatIntent.NONE.name()
            );
        }

        String sessionId = request.getSessionId();
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
            log.info("New session created: {}", sessionId);
        }

        String message = request.getMessage().toLowerCase();
        log.info("Chatbot message received: {}", message);

        if (message.contains("pay")) {
            return new ChatResponse(
                    sessionId,
                    "Your last trip pay is being processed.",
                    ChatIntent.NONE.name()
            );
        }

        if (message.contains("trip is done") || message.contains("trip completed")) {
            return new ChatResponse(
                    sessionId,
                    "Please upload your expense receipts.",
                    ChatIntent.UPLOAD_BILL.name()
            );
        }

        return new ChatResponse(
                sessionId,
                "Sorry, I didn’t understand your request.",
                ChatIntent.NONE.name()
        );
    }

    private ChatIntent detectIntent(String message) {

        if (message.equals("yes")
                || message.equals("y")
                || message.equals("confirm")
                || message.equals("ok")) {

            return ChatIntent.CONFIRM_YES;
        }

        return ChatIntent.UNKNOWN;
    }

    /* -------------------------------
       BILL / RECEIPT UPLOAD HANDLING
    -------------------------------- */
    public ChatResponse processBillUpload(MultipartFile file, String sessionId)
            throws DocumentProcessingException {

        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        if (file == null || file.isEmpty()) {
            throw new DocumentProcessingException("Uploaded file is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new DocumentProcessingException("Invalid file");
        }

        filename = filename.toLowerCase();

        if (!filename.endsWith(".jpg")
                && !filename.endsWith(".jpeg")
                && !filename.endsWith(".png")
                && !filename.endsWith(".pdf")) {
            throw new DocumentProcessingException("Unsupported file type");
        }

        // 🔹 Step 1: Extract raw text (OCR / PDF)
        String extractedText;
        if (filename.endsWith(".pdf")) {
            extractedText = pdfTextService.extractText(file);
        } else {
            extractedText = ocrService.extractText(file);
        }

        if (extractedText == null || extractedText.isBlank()) {
            throw new DocumentProcessingException("No readable text found in document");
        }

        log.info("===== EXTRACTED DOCUMENT TEXT =====");
        log.info(extractedText);

        // 🔹 Step 2: Interpret bill (AMOUNT + CURRENCY + INR CONVERSION)
        InterpretedBill bill;
        try {
            bill = podOcrInterpreter.interpret(extractedText);
        } catch (Exception ex) {
            log.error("Bill interpretation failed", ex);
            return new ChatResponse(
                    sessionId,
                    "This document does not look like a valid expense receipt. " +
                            "Please upload a fuel, toll, or parking bill with a visible amount.",
                    ChatIntent.UPLOAD_AGAIN.name()
            );
        }

        // 🔹 Step 3: Respond to chatbot with FINAL normalized data
        return new ChatResponse(
                sessionId,
                "I found a " + bill.getCategory() +
                        " expense of " + bill.getCurrency() + " " +
                        bill.getAmount() +
                        " (₹" + bill.getAmountInINR() + "). Please confirm.",
                ChatIntent.CONFIRM_BILL.name()
        );
    }
    }
