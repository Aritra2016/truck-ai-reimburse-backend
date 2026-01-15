package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.service.PodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pods")
public class PodController {

    private final PodService podService;

    public PodController(PodService podService) {
        this.podService = podService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam Long tripId,
            @RequestParam Long destinationId,
            @RequestParam String destinationName,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                podService.uploadPod(tripId, destinationId, destinationName, file)
        );

    }

}
