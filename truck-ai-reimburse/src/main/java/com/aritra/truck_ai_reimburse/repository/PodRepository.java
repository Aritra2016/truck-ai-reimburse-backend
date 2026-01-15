package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.PodDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PodRepository extends JpaRepository<PodDocument, Long> {

    Optional<PodDocument> findByDestinationId(Long destinationId);
}
