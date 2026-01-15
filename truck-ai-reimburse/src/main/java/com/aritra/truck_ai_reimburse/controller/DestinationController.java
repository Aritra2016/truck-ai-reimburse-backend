package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.service.DestinationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {
    private final DestinationService destinationService;
    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }
    // Driver clicks "Reached Destination"
    @PostMapping("/{id}/arrive")
    public ResponseEntity<?> arrive(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(
                destinationService.markArrived(id)
        );
    }
    // Used by chatbot / UI
    @GetMapping("/current/{tripId}")
    public ResponseEntity<?> current(@PathVariable Long tripId) {
        return ResponseEntity.ok(
                destinationService.getCurrentDestination(tripId)
        );
    }

}
