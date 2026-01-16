package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.CreateTripRequestDTO;
import com.aritra.truck_ai_reimburse.DTOs.TripResponseDTO;
import com.aritra.truck_ai_reimburse.Domain.Driver;
import com.aritra.truck_ai_reimburse.Domain.LedgerEvents;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
import com.aritra.truck_ai_reimburse.repository.DriverRepository;
import com.aritra.truck_ai_reimburse.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final LedgerService ledgerService;
    private final DestinationService destinationService;

    public TripService(
            TripRepository tripRepository,
            DriverRepository driverRepository,
            LedgerService ledgerService, DestinationService destinationService
    ) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.ledgerService = ledgerService;
        this.destinationService = destinationService;
    }

//    @Transactional
//    public Trip saveTrip(Trip trip) {
//        Long driverId = trip.getDriver().getId();
//        Driver driver = driverRepository.findById(driverId)
//                .orElseThrow(() -> new RuntimeException("Driver not found with id " + driverId));
//        trip.setDriver(driver);
//        trip.setStatus(TripStatus.IN_PROGRESS);
//        return tripRepository.save(trip);
//    }

    public List<TripResponseDTO> getTripsByDriver(Long driverId) {

        List<Trip> trips = tripRepository.findByDriver_Id(driverId);

        return trips.stream().map(trip -> {
            TripResponseDTO dto = new TripResponseDTO();
            dto.setTripId(trip.getId());
            dto.setTripNumber(trip.getTripNumber());
            dto.setOrigin(trip.getOrigin());
            dto.setDestination(trip.getDestination());
            dto.setStatus(trip.getStatus());
            dto.setTotalAmount(trip.getTotalAmount());
            return dto;
        }).toList();
    }

    @Transactional
    public Trip createTrip(CreateTripRequestDTO dto) {

        // STEP 1️⃣ Fetch Driver (Repository → Entity)
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver not found with id " + dto.getDriverId()
                        )
                );

        // STEP 2️⃣ Create Trip & set Driver ENTITY
        Trip trip = new Trip();
        trip.setTripNumber(dto.getTripNumber());
        trip.setDriver(driver);                 // ✅ FIX HERE
        trip.setOrigin(dto.getOrigin());
        trip.setDestination(dto.getDestination());
        trip.setPickupTime(dto.getPickupTime());
        trip.setTotalAmount(dto.getTotalAmount());
        trip.setStatus(TripStatus.CREATED);

        // STEP 3️⃣ Save Trip
        trip = tripRepository.save(trip);

        // STEP 4️⃣ Resolve stops (OPTION-2)
        List<String> routeStops =
                dto.getStops() != null && !dto.getStops().isEmpty()
                        ? dto.getStops()
                        : List.of(dto.getOrigin(), dto.getDestination());

        // STEP 5️⃣ Create Destinations
        destinationService.createDestinations(
                trip.getId(),
                routeStops
        );

        return trip;
    }

    @Transactional
    public Trip completeTrip(Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new IllegalStateException("Trip not in progress");
        }

        trip.setStatus(TripStatus.COMPLETED);
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