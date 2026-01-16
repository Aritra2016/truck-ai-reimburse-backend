package com.aritra.truck_ai_reimburse.service;


import com.aritra.truck_ai_reimburse.Domain.ReimbursementSession;
import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import com.aritra.truck_ai_reimburse.repository.ReimbursementSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReimbursementSessionService {

    private final ReimbursementSessionRepository sessionRepository;

    public ReimbursementSession getOrCreateSession(String sessionId, String tripId) {

        return sessionRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    ReimbursementSession session = ReimbursementSession.builder()
                            .sessionId(sessionId)
                            .tripId(tripId)
                            .status(ReimbursementStatus.POD_PENDING)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return sessionRepository.save(session);
                });
    }

    public void updateStatus(ReimbursementSession session,
                             ReimbursementStatus status) {
        session.setStatus(status);
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

}
