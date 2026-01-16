package com.aritra.truck_ai_reimburse.service;


import com.aritra.truck_ai_reimburse.Domain.PodDocument;
import com.aritra.truck_ai_reimburse.enums.PodStatus;
import com.aritra.truck_ai_reimburse.repository.PodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PodUploadService {

    private final PodRepository podRepository;
    private final PodVerificationService verificationService;

    public PodDocument uploadAndVerify(PodDocument podDocument,
                                       String ocrText) {

        podDocument.setStatus(PodStatus.PENDING);
        PodDocument saved = podRepository.save(podDocument);

        return verificationService.verifyPod(saved, ocrText);
    }

}
