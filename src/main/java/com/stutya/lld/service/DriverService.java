package com.stutya.lld.service;

import com.stutya.lld.domain.Driver;
import com.stutya.lld.domain.DriverStatus;
import com.stutya.lld.domain.Location;
import com.stutya.lld.repository.DriverRepository;

import java.util.Optional;

public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void goOnline(String driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        driver.setStatus(DriverStatus.ONLINE);
        driverRepository.save(driver);
    }

    public void goOffline(String driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        driver.setStatus(DriverStatus.OFFLINE);
        driverRepository.save(driver);
    }

    public void updateLocation(String driverId, Location location) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        driver.setCurrentLocation(location);
        driver.setLastLocationUpdate(location.getTimestamp());
        driverRepository.save(driver);
    }

    public Optional<Driver> findById(String driverId) {
        return driverRepository.findById(driverId);
    }
}