package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.Domain.Driver;
import com.aritra.truck_ai_reimburse.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverRepository driverRepository;

    @PostMapping("/create")
    public Driver createDriver(@RequestBody Driver driver) {
        return driverRepository.save(driver);
    }

    @GetMapping("/getAll")
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @GetMapping("/getById/{id}")
    public Optional<Driver> getDriverById(@PathVariable Long id){
        return driverRepository.findById(id);
    }
}
