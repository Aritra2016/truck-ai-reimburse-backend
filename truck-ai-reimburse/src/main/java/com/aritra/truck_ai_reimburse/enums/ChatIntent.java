package com.aritra.truck_ai_reimburse.enums;

public enum ChatIntent {
    NONE,          // No action expected
    UPLOAD_BILL,   // Ask user to upload a receipt
    UPLOAD_AGAIN,  // Invalid receipt, retry upload
    CONFIRM_BILL,  // Ask user to confirm extracted bill
    PAYOUT_READY
}
