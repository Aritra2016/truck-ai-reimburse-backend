package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Destination;
import com.aritra.truck_ai_reimburse.enums.DestinationStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.DestinationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DestinationService {
    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository, TripService tripService) {
        this.destinationRepository = destinationRepository;

    }

    public void createDestinations(Long tripId, List<String> stops) {
        int seq = 1;
        for (String stop : stops) {
            Destination d = new Destination();
            d.setTripId(tripId);
            d.setName(stop);
            d.setSequence(seq++);
            d.setStatus(DestinationStatus.PENDING);
            destinationRepository.save(d);
        }
    }

//    public Destination markArrived(Long destinationId) throws BusinessException {
//        Destination d = getById(destinationId);
//        if (d.getStatus() != DestinationStatus.PENDING) {
//            throw new BusinessException(
//                    "Destination must be PENDING to mark ARRIVED"
//            );
//        }d.setStatus(DestinationStatus.ARRIVED);
//        d.setArrivedAt(LocalDateTime.now());
//        return destinationRepository.save(d);
//    }


    // 3️⃣ Mark VERIFIED (called ONLY from PodService)

    @Transactional
    public boolean markVerified(Long destinationId) throws BusinessException {

        Destination d = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new BusinessException("Destination not found"));

        if (d.getStatus() != DestinationStatus.ARRIVED) {
            throw new BusinessException("Destination must be ARRIVED");
        }

        d.setStatus(DestinationStatus.VERIFIED);
        destinationRepository.save(d);

        //  ONLY CHECK, DO NOT COMPLETE TRIP HERE
        return destinationRepository
                .findByTripIdOrderBySequenceAsc(d.getTripId())
                .stream()
                .allMatch(dest -> dest.getStatus() == DestinationStatus.VERIFIED);
    }

    // ✔ mark ARRIVED
    public Destination markArrived(Long destinationId) {
        Destination d = getById(destinationId);

        if (d.getStatus() != DestinationStatus.PENDING) {
            throw new BusinessException("Destination must be PENDING");
        }

        d.setStatus(DestinationStatus.ARRIVED);
        d.setArrivedAt(LocalDateTime.now());
        return destinationRepository.save(d);
    }

    // helper
    private Destination getById(Long id) throws BusinessException {
        return destinationRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Destination not found")
                );
    }

    public Destination getCurrentDestination(Long tripId) {

        List<Destination> list =
                destinationRepository.findByTripIdOrderBySequenceAsc(tripId);

        for (Destination d : list) {
            if (d.getStatus() != DestinationStatus.VERIFIED) {
                return d;
            }
        }
        return null;
    }
}
