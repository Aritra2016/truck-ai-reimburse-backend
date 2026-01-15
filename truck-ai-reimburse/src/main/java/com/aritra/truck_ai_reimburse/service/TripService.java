package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.LedgerEvents;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
import com.aritra.truck_ai_reimburse.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final LedgerService ledgerService;

    //constructor
    public TripService(TripRepository tripRepository, LedgerService ledgerService) {
        this.tripRepository = tripRepository;
        this.ledgerService = ledgerService;
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public List<Trip> getTripsByDriver(Long driverId) {
        return tripRepository.findByDriverId(driverId);
    }

    @Transactional
    public Trip completeTrip(Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != (String.valueOf(TripStatus.IN_PROGRESS))) {
            throw new IllegalStateException("Trip not in progress");
        }

        trip.setStatus(String.valueOf(TripStatus.COMPLETED));
        tripRepository.save(trip);

        ledgerService.recordEvent(
                trip,
                LedgerEvents.TRIP_COMPLETED,
                "Truck reached destination",
                "Auto update on trip completion"
        );

        return trip;
    }
}
