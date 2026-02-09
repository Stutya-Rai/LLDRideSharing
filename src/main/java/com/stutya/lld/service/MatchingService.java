package com.stutya.lld.service;

import com.stutya.lld.domain.Driver;
import com.stutya.lld.domain.DriverStatus;
import com.stutya.lld.domain.NotificationMessage;
import com.stutya.lld.domain.Ride;
import com.stutya.lld.domain.RideStatus;
import com.stutya.lld.domain.strategy.DriverMatchingStrategy;
import com.stutya.lld.repository.DriverRepository;
import com.stutya.lld.repository.RideRepository;
import com.stutya.lld.service.notification.NotificationService;

import java.util.List;
import java.util.Optional;

public class MatchingService {
    private static final int DEFAULT_MAX_RESULTS = 3;
    private static final long DRIVER_RESPONSE_TIMEOUT_MS = 30000; // 30 seconds

    private final DriverRepository driverRepository;
    private final RideRepository rideRepository;
    private final DriverMatchingStrategy matchingStrategy;
    private final LockService lockService;
    private final NotificationService notificationService;

    public MatchingService(DriverRepository driverRepository,
                           RideRepository rideRepository,
                           DriverMatchingStrategy matchingStrategy,
                           LockService lockService,
                           NotificationService notificationService) {
        this.driverRepository = driverRepository;
        this.rideRepository = rideRepository;
        this.matchingStrategy = matchingStrategy;
        this.lockService = lockService;
        this.notificationService = notificationService;
    }

    public Optional<Driver> matchDriver(Ride ride) {
        if (ride.getStatus() != RideStatus.REQUESTED) {
            return Optional.empty();
        }

        List<Driver> availableDrivers = driverRepository.findByStatus(DriverStatus.ONLINE); // get this into strategy
        List<Driver> suggestions = matchingStrategy.findMatchingDrivers(ride.getPickupLocation(), availableDrivers, DEFAULT_MAX_RESULTS);
        if (suggestions.isEmpty()) {
            return Optional.empty();
        }

        for (Driver driver : suggestions) {
            String lockKey = "driver_lock_" + driver.getId();
            boolean acquired = lockService.acquire(lockKey, 200);
            if (!acquired) {
                continue;
            }
            try {
                Driver latestDriver = driverRepository.findById(driver.getId()).orElse(driver);
                if (latestDriver.getStatus() != DriverStatus.ONLINE) {
                    continue;
                }
                latestDriver.setCurrentLocation(driver.getCurrentLocation());
                driverRepository.save(latestDriver);
                ride.setStatus(RideStatus.REQUESTED);

                // Send notification to driver (don't assign yet - wait for acceptance)
                notificationService.send(new NotificationMessage(
                        latestDriver.getId(),
                        "New ride request",
                        "Ride " + ride.getId() + " is available. Please accept or decline."
                ));

                // Wait for driver response (accept/decline) or timeout
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < DRIVER_RESPONSE_TIMEOUT_MS || ride.getStatus() == RideStatus.DENIED) {
                    Ride currentRide = rideRepository.findById(ride.getId()).orElse(ride);

                    // Check if driver accepted (driverAccept will assign and accept)
                    if (currentRide.getStatus() == RideStatus.ACCEPTED &&
                            latestDriver.getId().equals(currentRide.getDriverId())) {
                        // Driver accepted - assignment is done by driverAccept, return and break
                        return Optional.of(latestDriver);
                    }

                    // Check if ride was cancelled
                    if (currentRide.getStatus() == RideStatus.CANCELLED) {
                        return Optional.empty();
                    }

                    // Check if another driver was assigned (shouldn't happen, but safety check)
                    if (currentRide.getDriverId() != null && !currentRide.getDriverId().equals(latestDriver.getId())) {
                        break; // Another driver was assigned, move to next
                    }

                    // Wait a bit before checking again
                    try {
                        Thread.sleep(500); // Poll every 500ms
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                // Timeout - check final status one more time
                Ride finalRide = rideRepository.findById(ride.getId()).orElse(ride);
                if (finalRide.getStatus() == RideStatus.ACCEPTED &&
                        latestDriver.getId().equals(finalRide.getDriverId())) {
                    return Optional.of(latestDriver);
                }
                if (finalRide.getStatus() == RideStatus.CANCELLED) {
                    return Optional.empty();
                }
                // Timeout - driver didn't respond, continue to next driver
                // (No assignment was made, so nothing to reset)
            } finally {
                lockService.release(lockKey);
            }
        }

        return Optional.empty();
    }

    public void releaseDriver(String driverId) {
        if (driverId == null) {
            return;
        }
        driverRepository.findById(driverId).ifPresent(driver -> {
            driver.setStatus(DriverStatus.ONLINE);
            driverRepository.save(driver);
        });
    }
}