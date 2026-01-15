package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip,Long> {

    Optional<Trip> findByTripNumber(String tripNumber);
      List<Trip> findByStatus(String status);
//    List<Trip> findByDriverId(Long driverId);
//
        List<Trip> findByDriver_Id(Long driverId);

   // List<Trip> findByDriver_Driver_id(Long driverId);
}
