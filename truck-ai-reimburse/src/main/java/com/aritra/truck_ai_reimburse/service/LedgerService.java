package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Trip;
import org.springframework.stereotype.Service;


public interface LedgerService {
    void recordEvent(Trip trip,
                     String eventType,
                     String eventDetails,
                     String remarks);
}
