package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DestinationRepository extends JpaRepository<Destination,Long> {

    List<Destination> findByTripIdOrderBySequenceAsc(Long tripId);

    Optional<Destination> findByTripIdAndSequence(
            Long tripId, Integer sequence
    );
}
