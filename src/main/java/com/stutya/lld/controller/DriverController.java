package com.stutya.lld.controller;

import com.stutya.lld.domain.Location;
import com.stutya.lld.service.DriverService;
import com.stutya.lld.service.RideService;

public class DriverController {
    private final DriverService driverService;
    private final RideService rideService;

    public DriverController(DriverService driverService, RideService rideService) {
        this.driverService = driverService;
        this.rideService = rideService;
    }

    public void goOnline(String driverId) {
        driverService.goOnline(driverId);
    }

    public void goOffline(String driverId) {
        driverService.goOffline(driverId);
    }

    public void updateLocation(String driverId, Location location) {
        driverService.updateLocation(driverId, location);
    }

    public void acceptRide(String rideId, String driverId) {
        rideService.driverAccept(rideId, driverId);
    }

    public void declineRide(String rideId, String driverId) {
        rideService.driverDecline(rideId, driverId);
    }

    public void startRide(String rideId, String driverId) {
        rideService.startRide(rideId, driverId);
    }

    public void completeRide(String rideId, String driverId) {
        rideService.completeRide(rideId, driverId);
    }
}