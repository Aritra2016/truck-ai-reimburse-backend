package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.DTOs.CreateTripRequestDTO;
import com.aritra.truck_ai_reimburse.DTOs.TripResponseDTO;
import com.aritra.truck_ai_reimburse.Domain.Driver;
import com.aritra.truck_ai_reimburse.Domain.LedgerEvents;
import com.aritra.truck_ai_reimburse.Domain.Trip;
import com.aritra.truck_ai_reimburse.enums.TripStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.DestinationRepository;
import com.aritra.truck_ai_reimburse.repository.DriverRepository;
import com.aritra.truck_ai_reimburse.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
   // private final DestinationRepository destinationRepository;
    private final LedgerService ledgerService;

    public TripService(
            TripRepository tripRepository,
            DriverRepository driverRepository, DestinationRepository destinationRepository,
            LedgerService ledgerService, DestinationService destinationService, DestinationService destinationService1
    ) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
     //   this.destinationRepository = destinationRepository;
        this.ledgerService = ledgerService;

    }
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

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver not found with id " + dto.getDriverId()
                        )
                );
        Trip trip = new Trip();
        trip.setTripNumber(dto.getTripNumber());
        trip.setDriver(driver);                 // ✅ FIX HERE
        trip.setOrigin(dto.getOrigin());
        trip.setDestination(dto.getDestination());
        trip.setPickupTime(dto.getPickupTime());
        trip.setTotalAmount(dto.getTotalAmount());
        trip.setStatus(TripStatus.CREATED);
        // STEP 3️⃣ Save Trip
        return tripRepository.save(trip);


    }
//    @Transactional
//    public Trip completeTrip(Long tripId) {
//        Trip trip = tripRepository.findById(tripId)
//                .orElseThrow(() -> new RuntimeException("Trip not found"));
//        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
//            throw new IllegalStateException("Trip not in progress");
//        }
//        trip.setStatus(TripStatus.COMPLETED);
//        tripRepository.save(trip);
//        ledgerService.recordEvent(
//                trip,
//                LedgerEvents.TRIP_COMPLETED,
//                "Truck reached destination",
//                "Auto update on trip completion"
//        );return trip;}

    @Transactional
    public void markTripCompleted(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException("Trip not found"));
        if (trip.getStatus() == TripStatus.COMPLETED) {
            return;
        }

        trip.setStatus(TripStatus.COMPLETED);
        tripRepository.save(trip);

        ledgerService.recordEvent(
                trip,
                LedgerEvents.TRIP_COMPLETED,
                "All destinations verified",
                "AUTO_SYSTEM"
        );
    }


}