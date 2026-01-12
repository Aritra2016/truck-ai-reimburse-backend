package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.DTOs.DashboardMetricsDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/metrics")
    public DashboardMetricsDTO getMetrics() {
        return DashboardMetricsDTO.builder()
                .totalTrips(120)
                .completedTrips(95)
                .totalPaidAmount(150000)
                .pendingAmount(12000)
                .openDisputes(4)
                .pendingReceipts(7)
                .build();
    }

}
