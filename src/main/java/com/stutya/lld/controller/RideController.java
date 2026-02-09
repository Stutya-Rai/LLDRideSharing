package com.stutya.lld.controller;

import com.stutya.lld.domain.FareEstimateResponse;
import com.stutya.lld.domain.Location;
import com.stutya.lld.domain.Ride;
import com.stutya.lld.domain.RideRequest;
import com.stutya.lld.domain.RideStatusResponse;
import com.stutya.lld.service.RideService;

public class RideController {
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    public FareEstimateResponse getFareEstimate(Location pickup, Location dropoff) {
        return rideService.estimateFare(pickup, dropoff);
    }

    public Ride requestRide(RideRequest request) {
        return rideService.requestRide(request);
    }

    public RideStatusResponse getRideStatus(String rideId) {
        return rideService.getRideStatus(rideId);
    }

    public void cancelRide(String rideId, String reason) {
        rideService.cancelRide(rideId, reason);
    }
}
