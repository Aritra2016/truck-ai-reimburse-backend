package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.DTOs.CreateTripRequestDTO;
import com.aritra.truck_ai_reimburse.DTOs.TripResponseDTO;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.service.DestinationService;
import com.aritra.truck_ai_reimburse.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final DestinationService destinationService;


    // CREATE
    @PostMapping("/create")
    public Trip createTrip(@RequestBody CreateTripRequestDTO dto) {
        Trip trip = tripService.createTrip(dto);

        List<String> stops =
                dto.getStops() != null && !dto.getStops().isEmpty()
                        ? dto.getStops()
                        : List.of(dto.getOrigin(), dto.getDestination());

        destinationService.createDestinations(trip.getId(), stops);

        return trip;
    }



    //    @GetMapping("/driver/{driverId}")
//    public List<TripResponseDTO> getTripsByDriver(@PathVariable Long driverId) {
//        return tripService.getTripsByDriver(driverId);
//    }
//    @PutMapping("/{tripId}/complete")
//    public Trip completeTrip(@PathVariable Long tripId) {
//        return tripService.completeTrip(tripId);
//    }
}




