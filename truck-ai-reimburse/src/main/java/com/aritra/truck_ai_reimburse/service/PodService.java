package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Destination;
import com.aritra.truck_ai_reimburse.Domain.PodDocument;
import com.aritra.truck_ai_reimburse.Interpreter.PodOcrInterpreter;
import com.aritra.truck_ai_reimburse.enums.DestinationStatus;
import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.PodRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class PodService {

    private final PodRepository podRepository;
    private final OCRService ocrService;
    private final PodOcrInterpreter interpreter;
    private final DestinationService destinationService;

    public PodService(PodRepository podRepository, OCRService ocrService, PodOcrInterpreter interpreter, DestinationService destinationService) {
        this.podRepository = podRepository;
        this.ocrService = ocrService;
        this.interpreter = interpreter;
        this.destinationService = destinationService;
    }

    public PodDocument uploadPod(
            Long tripId,
            Long destinationId,
            String destinationName,
            MultipartFile file
    ) {

        Destination destination = destinationService.getCurrentDestination(tripId);

        if (!destination.getId().equals(destinationId)) {
            throw new BusinessException("POD can be uploaded only for current destination");
        }

        if (destination.getStatus() != DestinationStatus.ARRIVED) {
            throw new BusinessException("Destination must be ARRIVED before POD upload");
        }

        // OCR extract
        String rawText = ocrService.extractRawText(file);

        boolean verified = interpreter.isPodValid(rawText, destinationName);

        PodDocument pod = new PodDocument();
        pod.setTripId(tripId);
        pod.setDestinationId(destinationId);
        pod.setDocumentUrl("stored-file-path"); // assume stored elsewhere
        pod.setRawOcrText(rawText);
        pod.setUploadedAt(LocalDateTime.now());

        if (verified) {
            pod.setOcrStatus(OcrStatus.VERIFIED);
            pod.setVerifiedAt(LocalDateTime.now());

            // 🔑 VERY IMPORTANT
            destinationService.markVerified(destinationId);

        } else {
            pod.setOcrStatus(OcrStatus.FAILED);
        }

        return podRepository.save(pod);
    }
}
