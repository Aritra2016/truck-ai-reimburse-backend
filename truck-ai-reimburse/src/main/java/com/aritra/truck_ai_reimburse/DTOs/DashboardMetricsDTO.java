package com.aritra.truck_ai_reimburse.DTOs;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMetricsDTO {

    private long totalTrips;
    private long completedTrips;

    private double totalPaidAmount;
    private double pendingAmount;

    private long openDisputes;
    private long pendingReceipts;
}
