package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/create")
    public Trip createTrip(@RequestBody Trip trip) {
        return tripService.saveTrip(trip);
    }

    @GetMapping("/driver/{driverId}")
    public List<Trip> getTripsByDriver(@PathVariable Long driverId) {
        return tripService.getTripsByDriver(driverId);
    }

    @PutMapping("/{tripId}/complete")
    public Trip completeTrip(@PathVariable Long tripId) {
        return tripService.completeTrip(tripId);
    }

}
