package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.DestinationRepository;
import com.aritra.truck_ai_reimburse.service.DestinationService;
import com.aritra.truck_ai_reimburse.service.PodService;
import com.aritra.truck_ai_reimburse.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pods")
public class PodController {
    private final PodService podService;
    private final DestinationRepository destinationRepository;
    private final DestinationService destinationService;
    private final TripService tripService;

    public PodController(PodService podService, DestinationRepository destinationRepository, DestinationService destinationService, TripService tripService) {
        this.podService = podService;
        this.destinationRepository = destinationRepository;
        this.destinationService = destinationService;
        this.tripService = tripService;
    }

    @PostMapping("/{destinationId}/verify")
    public ResponseEntity<?> verifyDestination(
            @PathVariable Long destinationId) throws BusinessException {
        boolean allDone = destinationService.markVerified(destinationId);
        if (allDone) {
            Long tripId = destinationRepository
                    .findById(destinationId)
                    .orElseThrow()
                    .getTripId();
            tripService.markTripCompleted(tripId);
        } return ResponseEntity.ok("Destination verified");
    }


//    @PostMapping("/upload")
//    public ResponseEntity<?> upload(
//            @RequestParam Long tripId,
//            @RequestParam Long destinationId,
//            @RequestParam String destinationName,
//            @RequestParam MultipartFile file) {
//        return ResponseEntity.ok(
//                podService.uploadPod(tripId, destinationId, destinationName, file));
//    }

}
