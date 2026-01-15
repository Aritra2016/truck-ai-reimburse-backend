package com.aritra.truck_ai_reimburse.enums;

public enum OcrStatus {

    PENDING,    // Uploaded, OCR not yet processed
    VERIFIED,   // OCR successful & business validation passed
    FAILED      // OCR processed but validation failed
}
