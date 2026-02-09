package com.stutya.lld.domain.strategy;

import com.stutya.lld.domain.Driver;
import com.stutya.lld.domain.Location;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NearestDriverStrategy implements DriverMatchingStrategy {
    @Override
    public List<Driver> findMatchingDrivers(Location pickup, List<Driver> candidates, int maxResults) {
        return candidates.stream()
                .sorted(Comparator.comparingDouble(driver -> distanceKm(pickup, driver.getCurrentLocation())))
                .limit(maxResults)
                .collect(Collectors.toList());
    }

    private double distanceKm(Location a, Location b) {
        if (a == null || b == null) {
            return Double.MAX_VALUE;
        }
        double latDiff = a.getLatitude() - b.getLatitude();
        double lonDiff = a.getLongitude() - b.getLongitude();
        // TODO: Replace with haversine distance for better accuracy.
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff) * 111;
    }
}
