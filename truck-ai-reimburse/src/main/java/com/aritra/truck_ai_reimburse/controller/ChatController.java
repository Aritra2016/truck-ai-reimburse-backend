package com.aritra.truck_ai_reimburse.controller;


import com.aritra.truck_ai_reimburse.Domain.ChatRequest;
import com.aritra.truck_ai_reimburse.Domain.ChatResponse;
import com.aritra.truck_ai_reimburse.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatbotService chatbotService;

    @PostMapping("/chatting")
    public ChatResponse handleMessage(@RequestBody ChatRequest request){
      return chatbotService.processMessage(request);
    }

    @PostMapping("/uploadbill")
    public ChatResponse uploadBill(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String sessionId
    ) {
        return chatbotService.processBillUpload(file, sessionId);
    }

}
