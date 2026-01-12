package com.aritra.truck_ai_reimburse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notifyDriver(Long driverId, String message) {
        log.info("Notification sent to driver {}: {}", driverId, message);
    }
}
