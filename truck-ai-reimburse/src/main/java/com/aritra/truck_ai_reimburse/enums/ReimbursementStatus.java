package com.aritra.truck_ai_reimburse.enums;

public enum ReimbursementStatus {
    DRAFT,                  // expenses uploading + OCR running
    READY_FOR_SETTLEMENT,   // driver confirmed
    CANCELLED               // optional (future use)
}
