package com.stutya.lld.domain.strategy;

import com.stutya.lld.domain.Location;

public class BasePricingStrategy implements PricingStrategy {
    private final long baseFareMinor = 2000; // 20.00 in minor units
    private final long perKmMinor = 800; // 8.00 per km
    private final long perMinuteMinor = 200; // 2.00 per min

    @Override
    public long calculateFare(Location pickup, Location dropoff, double distanceKm, long durationSec) {
        long distanceComponent = (long) (distanceKm * perKmMinor);
        long timeComponent = (durationSec / 60) * perMinuteMinor;
        return Math.max(baseFareMinor, baseFareMinor + distanceComponent + timeComponent);
    }
}