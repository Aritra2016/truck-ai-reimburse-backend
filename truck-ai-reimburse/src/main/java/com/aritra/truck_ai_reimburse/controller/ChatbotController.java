package com.aritra.truck_ai_reimburse.controller;


import com.aritra.truck_ai_reimburse.DTOs.ChatMessageDTO;
import com.aritra.truck_ai_reimburse.Domain.ChatRequest;
import com.aritra.truck_ai_reimburse.Domain.ChatResponse;
import com.aritra.truck_ai_reimburse.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/message")
    public ChatResponse processMessage(@RequestBody ChatRequest request) {
        return chatbotService.processMessage(request);
    }

}
