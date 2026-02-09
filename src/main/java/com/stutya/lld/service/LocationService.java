package com.stutya.lld.service;

import com.stutya.lld.domain.Location;
import com.stutya.lld.repository.LocationRepository;

public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void updateDriverLocation(String driverId, Location location) {
        locationRepository.saveLocation(driverId, location);
    }

    public Location getDriverLocation(String driverId) {
        return locationRepository.getLatestLocation(driverId);
    }

    public double calculateDistanceKm(Location from, Location to) {
        if (from == null || to == null) {
            return 0d;
        }
        double latDiff = from.getLatitude() - to.getLatitude();
        double lonDiff = from.getLongitude() - to.getLongitude();
        // TODO: Use map provider to calculate distance.
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff) * 111;
    }

    public long estimateDurationSec(double distanceKm) {
        double avgSpeedKmh = 35; // simple assumption // fetch from Map Provider again
        double hours = distanceKm / avgSpeedKmh;
        return (long) (hours * 3600);
    }
}