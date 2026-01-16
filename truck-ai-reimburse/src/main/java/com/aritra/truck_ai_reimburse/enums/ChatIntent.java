package com.aritra.truck_ai_reimburse.enums;

public enum ChatIntent {
    NONE,                   // No action
    TRIP_COMPLETED,         // Driver says trip completed
    UPLOAD_POD,             // Ask for POD upload
    POD_VERIFIED,           // POD verified successfully
    POD_FAILED,             // POD invalid
    UPLOAD_EXPENSE,         // Upload reimbursement bills
    CONFIRM_EXPENSE,        // Confirm extracted bill
    ALL_DOCS_UPLOADED,      // User says all docs uploaded
    UPLOAD_BILL, PAYOUT_READY  ,
    UPLOAD_AGAIN ,// Reimbursement ready
    CONFIRM_BILL
}
