package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Dispute;
import com.aritra.truck_ai_reimburse.repository.DisputeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisputeService {

    private final DisputeRepository disputeRepository;

    public Dispute raiseDispute(Dispute dispute) {
        dispute.setStatus("OPEN");
        dispute.setCreatedAt(LocalDateTime.now());
        return disputeRepository.save(dispute);
    }

}
