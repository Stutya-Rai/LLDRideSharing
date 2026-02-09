package com.stutya.lld.domain;

public class RideStatusResponse {
    private final String rideId;
    private final RideStatus status;
    private final String driverId;
    private final String driverName;
    private final Location driverLocation;
    private final long estimatedFare;
    private final long updatedAt;

    public RideStatusResponse(String rideId, RideStatus status, String driverId, String driverName, Location driverLocation, long estimatedFare, long updatedAt) {
        this.rideId = rideId;
        this.status = status;
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverLocation = driverLocation;
        this.estimatedFare = estimatedFare;
        this.updatedAt = updatedAt;
    }

    public RideStatusResponse(String rideId,
                              RideStatus status,
                              String driverId,
                              String driverName,
                              Location driverLocation,
                              long estimatedFare,
                              long legacyFare,
                              long updatedAt) {
        this(rideId, status, driverId, driverName, driverLocation, estimatedFare, updatedAt);
    }

    public String getRideId() {
        return rideId;
    }

    public RideStatus getStatus() {
        return status;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public Location getDriverLocation() {
        return driverLocation;
    }

    public long getEstimatedFare() {
        return estimatedFare;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}