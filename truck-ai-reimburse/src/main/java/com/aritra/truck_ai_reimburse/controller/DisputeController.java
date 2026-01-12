package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.Domain.Dispute;
import com.aritra.truck_ai_reimburse.service.DisputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/disputes")
@RequiredArgsConstructor
public class DisputeController {

    private final DisputeService disputeService;

    @PostMapping
    public Dispute raiseDispute(@RequestBody Dispute dispute) {
        return disputeService.raiseDispute(dispute);
    }
}
