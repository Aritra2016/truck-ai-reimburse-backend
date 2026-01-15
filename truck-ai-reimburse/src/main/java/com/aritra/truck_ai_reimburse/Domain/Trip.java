package com.aritra.truck_ai_reimburse.Domain;

import com.aritra.truck_ai_reimburse.enums.TripStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trip_id;

    @Column(nullable = false, unique = true)
    private String tripNumber;

    // Driver Mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    private String origin;
    private String destination;

    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus tripstatus;
    private  String status;
    // PLANNED, IN_TRANSIT, COMPLETED
    private BigDecimal totalAmount;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Long trip_id) {
        this.trip_id = trip_id;
    }

    public TripStatus getTripstatus() {
        return tripstatus;
    }

    public void setTripstatus(TripStatus tripstatus) {
        this.tripstatus = tripstatus;
    }

    public String getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
