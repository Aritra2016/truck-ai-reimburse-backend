package com.aritra.truck_ai_reimburse.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_ledger")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ledgerRef;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    private String eventType; // PAY_CALCULATED, EXPENSE_ADDED, PAYOUT_DONE

    @Column(columnDefinition = "TEXT")
    private String eventDetails;

    private LocalDateTime eventTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLedgerRef() {
        return ledgerRef;
    }

    public void setLedgerRef(String ledgerRef) {
        this.ledgerRef = ledgerRef;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
}
