package com.aritra.truck_ai_reimburse.service;

import org.springframework.stereotype.Service;

@Service
public class ComplianceService {

    public boolean validateIFTA(String jurisdiction) {
        return jurisdiction != null && !jurisdiction.isBlank();
    }

    public boolean validateELDData(boolean hasLocationData) {
        return hasLocationData;
    }
}
