package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.AuditLog;
import com.aritra.truck_ai_reimburse.Domain.PodDocument;
import com.aritra.truck_ai_reimburse.Interpreter.PodOcrInterpreter;
import com.aritra.truck_ai_reimburse.enums.PodStatus;
import com.aritra.truck_ai_reimburse.repository.AuditRepository;
import com.aritra.truck_ai_reimburse.repository.PodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PodVerificationService {

    private final PodRepository podRepository;
    private final PodOcrInterpreter podOcrInterpreter;
    private final AuditRepository auditRepository;

    public PodDocument verifyPod(PodDocument pod, String ocrText) {

        boolean valid = podOcrInterpreter.isPodValid(ocrText);

        pod.setStatus(valid ? PodStatus.VERIFIED : PodStatus.FAILED);
        PodDocument updated = podRepository.save(pod);

        auditRepository.save(
                AuditLog.builder()
                        .entityName("POD")
                        .entityId(updated.getId().toString())
                        .action(valid ? "POD_VERIFIED" : "POD_FAILED")
                        .performedAt(LocalDateTime.now())
                        .build()
        );

        return updated;
    }

}
