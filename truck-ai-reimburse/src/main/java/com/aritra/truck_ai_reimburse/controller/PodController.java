package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.DTOs.PodUploadResponseDTO;
import com.aritra.truck_ai_reimburse.Domain.PodDocument;
import com.aritra.truck_ai_reimburse.enums.PodStatus;
import com.aritra.truck_ai_reimburse.service.OCRService;
import com.aritra.truck_ai_reimburse.service.PdfTextService;
import com.aritra.truck_ai_reimburse.service.PodUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pod")
@RequiredArgsConstructor
public class PodController {

    private final PodUploadService podUploadService;
    private final OCRService ocrService;
    private final PdfTextService pdfTextService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public PodUploadResponseDTO uploadPod(
            @RequestParam("file") MultipartFile file,
            @RequestParam String sessionId,
            @RequestParam String tripId,
            @RequestParam String deliveryLocation) {

        String extractedText =
                file.getOriginalFilename().endsWith(".pdf")
                        ? pdfTextService.extractText(file)
                        : ocrService.extractText(file);

        PodDocument pod = PodDocument.builder()
                .sessionId(sessionId)
                .tripId(tripId)
                .deliveryLocation(deliveryLocation)
                .build();

        PodDocument verifiedPod =
                podUploadService.uploadAndVerify(pod, extractedText);

        return PodUploadResponseDTO.builder()
                .sessionId(sessionId)
                .tripId(tripId)
                .podStatus(verifiedPod.getStatus())
                .deliveryLocation(verifiedPod.getDeliveryLocation())
                .message(
                        verifiedPod.getStatus() == PodStatus.VERIFIED
                                ? "POD verified successfully"
                                : "Invalid POD document"
                )
                .build();
    }

}
